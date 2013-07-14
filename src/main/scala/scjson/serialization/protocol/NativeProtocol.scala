package scjson.serialization

import scjson._

import JSONSerializationUtil._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit object UnitFormat extends Format[Unit] {
		def write(out:Unit):JSONValue	= JSONObject.empty
		def read(in:JSONValue):Unit		= ()
	}
	
	implicit object NullFormat extends Format[Null] {
		def write(out:Null):JSONValue	= JSONNull
		def read(in:JSONValue):Null		= null
	}

	implicit object ByteFormat extends Format[Byte] {
		def write(out:Byte):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Byte		= numberValue(in).toByte
	}
	implicit object ShortFormat extends Format[Short] {
		def write(out:Short):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Short	= numberValue(in).toShort
	}
	implicit object IntFormat extends Format[Int] {
		def write(out:Int):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Int		= numberValue(in).toInt
	}
	implicit object LongFormat extends Format[Long] {
		def write(out:Long):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Long		= numberValue(in).toLong
	} 
	implicit object FloatFormat extends Format[Float] {
		def write(out:Float):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Float	= numberValue(in).toFloat
	}
	implicit object DoubleFormat extends Format[Double] {
		def write(out:Double):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):Double	= numberValue(in).toDouble
	}
	
	implicit object BigIntFormat extends Format[BigInt] {
		def write(out:BigInt):JSONValue		= JSONNumber(out)
		def read(in:JSONValue):BigInt		= numberValue(in).toBigInt
	}
	implicit object BigDecimalFormat  extends Format[BigDecimal] {
		def write(out:BigDecimal):JSONValue	= JSONNumber(out)
		def read(in:JSONValue):BigDecimal	= numberValue(in)
	}
	
	implicit object BooleanFormat extends Format[Boolean] {
		def write(out:Boolean):JSONValue	= JSONBoolean(out)
		def read(in:JSONValue):Boolean		= booleanValue(in)
	}
	
	implicit object CharFormat extends Format[Char] {
		def write(out:Char):JSONValue	= JSONString(out.toString)
		def read(in:JSONValue):Char		= stringValue(in).head
	}
	implicit object StringFormat extends Format[String] {
		def write(out:String):JSONValue	= JSONString(out)
		def read(in:JSONValue):String	= stringValue(in)
	}
}
