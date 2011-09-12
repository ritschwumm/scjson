package scjson

import java.lang.reflect.{ Modifier, Field, Constructor }

import scala.collection.mutable

import scutil.Functions._
import scutil.Implicits._
import scutil.Bijection

import scjson.reflection._

/**
purely reflection-based serialization and deserialization between a subset of Any and JSValue.

the subset contains native types, some collections and case classes.
case classes are tagged with a special property containing their class name.
*/
object JSMapper extends Bijection[Any,JSValue] {
	/** name of a property used in objects to transfer their type */
	val typeTag	= JSString("_")
	
	def write(data:Any):JSValue = data match {
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
			case (key:String, value)	=> (JSString(key), write(value))
			case (key,valu)				=> sys error ("map key is not a String: " + key)
		})
		case value:Seq[_]		=> JSArray(value map write)
		// TODO hack for Option
		case data:Option[_]		=> data map write getOrElse JSNull
		case data:AnyRef		=> 
			val clazz	= data.asInstanceOf[AnyRef].getClass
			val props	= reflect(clazz)
			val entries	= props.accessors map { property =>
				val accessor	= clazz getMethod (Mangling mangle property)
				require(accessor != null, "missing accessor: " + property)
				JSString(property)	-> write(accessor invoke data)
			}
			JSObject((Pair(typeTag, JSString(clazz.getName)) +: entries).toMap)
		case data	=> sys error ("cannot serialize: " + data)
	}
	
	def readAs[T](json:JSValue):T	=
			read(json).asInstanceOf[T] 
	
	def read(json:JSValue):Any	= json match {
		case JSString(value)	=> value
		case JSNumber(value)	=> value 
		case JSTrue				=> true
		case JSFalse			=> false
		case JSNull				=> null
		case JSArray(value)		=> value map read
		case JSObject(value)	=>
			if (value contains typeTag) {
				val	className	= value get typeTag collect { case JSString(_type) => _type } getOrError ("missing type tag " + typeTag)
				val clazz		= Class forName className
				if (clazz.getName endsWith "$") {
					// case object: get singleton instance
					clazz getDeclaredField "MODULE$" get null
				}
				else {
					val props	= reflect(clazz)
					val	rawArgs	= props.constructor map { property =>
						value get JSString(property) getOrError ("missing field " + property)
					}
					val boxedArgs	= rawArgs map { child => 
						boxValue(read(child)) 
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
				// without type tag, it's a Map
				value map { case (key,value) => (read(key), read(value)) } 
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
	
	/** unmangled names */
	private case class Reflected(constructor:Seq[String], accessors:Seq[String])

	private var reflectedCache	= mutable.HashMap.empty[Class[_], Reflected]
	
	// TODO improve error handling
	private def reflect(clazz:Class[_]):Reflected	= synchronized {
		reflectedCache get clazz getOrElse {
			val constructor	= Reflector constructor clazz getOrError ("cannot reflect class: "  + clazz)
			val accessors	= Reflector accessors   clazz
			Reflected(constructor, accessors) |>> {
				reflectedCache update (clazz, _)
			}
		}
	}
}
