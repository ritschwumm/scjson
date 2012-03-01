package scjson.serialization

import scjson._

import JSONSerializationUtil._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit object UnitJSONFormat extends JSONFormat[Unit] {
		def write(out:Unit):JSONValue	= JSONObject(Map.empty)
		def read(in:JSONValue):Unit		= ()
	}
	
	implicit object NullJSONFormat extends JSONFormat[Null] {
		def write(out:Null):JSONValue	= JSONNull
		def read(in:JSONValue):Null		= null
	}

	implicit object ByteJSONFormat extends JSONFormat[Byte] {
		def write(out:Byte):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Byte		= numberValue(in).toByte
	}
	implicit object ShortJSONFormat extends JSONFormat[Short] {
		def write(out:Short):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Short	= numberValue(in).toShort
	}
	implicit object IntJSONFormat extends JSONFormat[Int] {
		def write(out:Int):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Int		= numberValue(in).toInt
	}
	implicit object LongJSONFormat extends JSONFormat[Long] {
		def write(out:Long):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Long		= numberValue(in).toLong
	} 
	implicit object FloatJSONFormat extends JSONFormat[Float] {
		def write(out:Float):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Float	= numberValue(in).toFloat
	}
	implicit object DoubleJSONFormat extends JSONFormat[Double] {
		def write(out:Double):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Double	= numberValue(in).toDouble
	}
	
	implicit object BigIntJSONFormat extends JSONFormat[BigInt] {
		def write(out:BigInt):JSONValue		= JSONNumber(out)
		def read(in:JSONValue):BigInt		= numberValue(in).toBigInt
	}
	implicit object BigDecimalJSONFormat  extends JSONFormat[BigDecimal] {
		def write(out:BigDecimal):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):BigDecimal	= numberValue(in)
	}
	
	implicit object BooleanJSONFormat extends JSONFormat[Boolean] {
		def write(out:Boolean):JSONValue	= JSONBoolean(out)
		def read(in:JSONValue):Boolean		= booleanValue(in)
	}
	
	implicit object CharJSONFormat extends JSONFormat[Char] {
		def write(out:Char):JSONValue	= JSONString(out.toString)
		def read(in:JSONValue):Char		= stringValue(in).head
	}
	implicit object StringJSONFormat extends JSONFormat[String] {
		def write(out:String):JSONValue	= JSONString(out)
		def read(in:JSONValue):String	= stringValue(in)
	}
}
