package scjson

import java.lang.reflect.{ Modifier, Field, Constructor }

import scala.collection.mutable

import scutil.Functions._
import scutil.Implicits._
import scutil.Bijection

import scmirror._

/**
purely reflection-based serialization and deserialization between a subset of Any and JSONValue.

the subset contains native types, some collections and case classes.
case classes are tagged with a special property containing their class name.
*/
@deprecated("use typeclass-based serialization, see JSSerialization", "30sep11")
object JSONMapper extends Bijection[Any,JSONValue] {
	/** name of a property used in objects to transfer their type */
	val typeTag	= JSONString("")
	
	def write(data:Any):JSONValue = data match {
		case null				=> JSONNull
		// case data:JSONValue		=> data
		case data:Int			=> JSONNumber(data)
		case data:Long			=> JSONNumber(data)
		case data:Float			=> JSONNumber(data)
		case data:Double		=> JSONNumber(data)
		case data:BigInt		=> JSONNumber(data)       
		case data:BigDecimal	=> JSONNumber(data)
		case data:Boolean		=> JSONBoolean(data)
		case data:String		=> JSONString(data)
		// NOTE this needs to be a Map[String,_], other Maps could be serialized as Array[Pair[_]]
		case data:Map[_,_]		=> JSONObject(data.map {
			case (key:String, value)	=> (JSONString(key), write(value))
			case (key,valu)				=> sys error ("map key is not a String: " + key)
		})
		case value:Seq[_]		=> JSONArray(value map write)
		// TODO hack for Option
		case data:Option[_]		=> data map write getOrElse JSONNull
		case data:AnyRef		=> 
			val clazz	= data.asInstanceOf[AnyRef].getClass
			val props	= reflect(clazz)
			val entries	= props.accessors map { property =>
				val accessor	= clazz getMethod (Mangling mangle property)
				require(accessor != null, "missing accessor: " + property)
				JSONString(property)	-> write(accessor invoke data)
			}
			JSONObject((Pair(typeTag, JSONString(clazz.getName)) +: entries).toMap)
		case data	=> sys error ("cannot serialize: " + data)
	}
	
	def readAs[T](json:JSONValue):T	=
			read(json).asInstanceOf[T] 
	
	def read(json:JSONValue):Any	= json match {
		case JSONString(value)	=> value
		case JSONNumber(value)	=> value 
		case JSONBoolean(value)	=> value
		case JSONNull			=> null
		case JSONArray(value)	=> value map read
		case JSONObject(value)	=>
			if (value contains typeTag) {
				val	className	= value get typeTag collect { case JSONString(_type) => _type } getOrError ("missing type tag " + typeTag)
				val clazz		= Class forName className
				if (clazz.getName endsWith "$") {
					// case object: get singleton instance
					clazz getDeclaredField "MODULE$" get null
				}
				else {
					val props	= reflect(clazz)
					val	rawArgs	= props.constructor map { property =>
						value get JSONString(property) getOrError ("missing field " + property)
					}
					val boxedArgs	= rawArgs map { child => 
						read(child).boxed 
					}
					val constructions:Seq[()=>Any]	= 
							clazz.getConstructors.view
							.filter { _.getParameterTypes.size == boxedArgs.size }
							.flatMap { ctor:Constructor[_] =>
								val	boxedTypes	= ctor.getParameterTypes map { _.boxed }
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
	
	//------------------------------------------------------------------------------
	
	/** unmangled names */
	private case class Reflected(constructor:Seq[String], accessors:Seq[String])

	private var reflectedCache	= mutable.HashMap.empty[Class[_], Reflected]
	
	// TODO improve error handling
	private def reflect(clazz:Class[_]):Reflected	= synchronized {
		reflectedCache getOrElseUpdate (clazz, {
			val constructor	= Reflector constructor clazz getOrError ("cannot reflect class: "  + clazz)
			val accessors	= Reflector accessors   clazz
			Reflected(constructor, accessors)
		})
	}
}
