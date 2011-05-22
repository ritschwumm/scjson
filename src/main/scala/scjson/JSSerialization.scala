package scjson

import java.lang.reflect.{ Modifier, Field, Constructor }

import scala.collection.mutable

import scutil.Functions._
import scutil.ext.OptionImplicits._
import scutil.ext.BooleanImplicits._

import scjson.reflect._

/**
serializes and deserializes native types and case classes to JSON.
case classes are tagged with a special property containing their class name
*/
object JSSerialization {
	/** name of a property used in objects to transfer their type */
	val typeTag	= JSString("_type")
	
	def serialize(data:Any):JSValue = data match {
		case null				=> JSNull
		// case data:JSValue		=> data
		case data:Int			=> JSNumber(data)
		case data:Long			=> JSNumber(data)
		case data:Float			=> JSNumber(data)
		case data:Double		=> JSNumber(data)
		case data:BigInt		=> JSNumber(data)       
		case data:BigDecimal	=> JSNumber(data)
		case data:Boolean		=> JSBoolean(data)
		case data:String		=> JSString(data)
		// NOTE this needs to be a Map[String,_], other Maps could be serialized as Array[Pair[_]]
		case data:Map[_,_]		=> JSObject(data.map {
			case (key:String, value)	=> (JSString(key), serialize(value))
			case (key,valu)				=> sys error ("map key is not a String: " + key)
		})
		case value:Seq[_]		=> JSArray(value map serialize)
		// TODO hack for Option
		case data:Option[_]		=> data map serialize getOrElse JSNull
		case data:AnyRef		=> 
			val clazz	= data.asInstanceOf[AnyRef].getClass
			val props	= reflect(clazz)
			val entries	= props.outgoing map { property =>
				val accessor	= clazz getMethod property.mangled
				require(accessor != null, "missing accessor: " + property)
				JSString(property.plain)	-> serialize(accessor invoke data)
			}
			JSObject((Pair(typeTag, JSString(clazz.getName)) +: entries).toMap)
		case data	=> sys error ("cannot serialize: " + data)
	}
	
	def deserializeAs[T](json:JSValue):T	=
			deserialize(json).asInstanceOf[T] 
	
	def deserialize(json:JSValue):Any	= json match {
		case JSString(value)	=> value
		case JSNumber(value)	=> value 
		case JSTrue				=> true
		case JSFalse			=> false
		case JSNull				=> null
		case JSArray(value)		=> value map deserialize
		case JSObject(value)	=>
			if (value contains typeTag) {
				val	className	= value get typeTag collect { case JSString(_type) => _type } getOrError ("missing typetag " + typeTag)
				val clazz		= Class forName className
				if (clazz.getName endsWith "$") {
					// case object: get singleton instance
					clazz getDeclaredField "MODULE$" get null
				}
				else {
					val props	= reflect(clazz)
					val	rawArgs	= props.incoming map { property =>
						value get JSString(property.plain) getOrError ("missing field " + property)
					}
					val boxedArgs	= rawArgs map { child => 
						boxValue(deserialize(child)) 
					}
					val constructions:Seq[()=>Any]	= 
							clazz.getConstructors.view
							.filter { _.getParameterTypes.size == boxedArgs.size }
							.flatMap { ctor:Constructor[_] =>
								val	boxedTypes	= ctor.getParameterTypes map boxType
								val	coercedArgs	= boxedArgs zip boxedTypes map coerceValue
								coercedArgs forall { _.isDefined } guard thunk {
									val ctorArgs:Array[AnyRef]	= coercedArgs map { _.get } toArray;
									ctor newInstance (ctorArgs:_*) 
								}
							}
					
					// TODO fail if ambiguous?
					constructions.headOption.getOrError("missing constructor").apply()
				}
			}
			else {
				// without typetag, it's a Map
				value map { case (key,value) => (deserialize(key), deserialize(value)) } 
			}
	}
	
	private def coerceValue(trans:Pair[AnyRef,Class[_]]):Option[AnyRef] = trans match {
		// TODO hack for Option
		case (null,to)				if classOf[Option[_]] isAssignableFrom to	=> Some(None)
		// NOTE: fails for Option[T] if T is not a case class
		case (value,to)				if classOf[Option[_]] isAssignableFrom to	=> Some(Some(value))	
		case (null,_)															=> None
		case (value,to)				if to isAssignableFrom value.getClass		=> Some(value)
		case (value:BigDecimal, to)	if to == classOf[java.lang.Byte]			=> Some(value.byteValue.asInstanceOf[AnyRef])
		case (value:BigDecimal, to)	if to == classOf[java.lang.Short]			=> Some(value.shortValue.asInstanceOf[AnyRef])
		case (value:BigDecimal, to)	if to == classOf[java.lang.Integer]			=> Some(value.intValue.asInstanceOf[AnyRef])
		case (value:BigDecimal, to)	if to == classOf[java.lang.Long]			=> Some(value.longValue.asInstanceOf[AnyRef])
		case (value:BigDecimal, to)	if to == classOf[java.lang.Float]			=> Some(value.floatValue.asInstanceOf[AnyRef])
		case (value:BigDecimal, to)	if to == classOf[java.lang.Double]			=> Some(value.doubleValue.asInstanceOf[AnyRef])
		case _																	=> sys error (trans._1.getClass + " not assignable to " + trans._2 + " (2)")
	}
	
	private def boxValue(value:Any):AnyRef	= value.asInstanceOf[AnyRef]
	
	private def boxType(clazz:Class[_]):Class[_] = clazz match {
		case java.lang.Byte.TYPE		=> classOf[java.lang.Byte]
		case java.lang.Short.TYPE		=> classOf[java.lang.Short]
		case java.lang.Integer.TYPE		=> classOf[java.lang.Integer]             
		case java.lang.Long.TYPE		=> classOf[java.lang.Long]
		case java.lang.Float.TYPE		=> classOf[java.lang.Float]
		case java.lang.Double.TYPE		=> classOf[java.lang.Double]
		case java.lang.Boolean.TYPE		=> classOf[java.lang.Boolean]
		case java.lang.Character.TYPE	=> classOf[java.lang.Character]
		case java.lang.Void.TYPE		=> classOf[java.lang.Void]
		case x							=> x
	}
	
	//------------------------------------------------------------------------------
	
	// TODO improve error handling
	private def reflect(clazz:Class[_]):Reflected	= synchronized {
		reflectCaching(clazz) getOrError ("cannot reflect class: "  + clazz)
	}
	
	private val reflectCaching	= new Cache(Reflector.reflect)
	
	// BETTER generalize, see LRU
	private class Cache[S,T](create:S=>T) extends Function1[S,T] {
		private val data	= mutable.Map.empty[S,T]
		
		def apply(s:S):T	= data get s getOrElse {
			val	t	= create(s)
			data put (s,t)
			t
		}
	}
}
