package scjson.pickle.protocol

import scutil.lang._

import scjson.ast._
import scjson.pickle._

import JsonPickleUtil._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit lazy val UnitFormat		= Format[Unit]		(constant(JsonObject.empty),	constant(()))
	implicit lazy val NullFormat		= Format[Null]		(constant(JsonNull),			constant(null))
	
	implicit lazy val ByteFormat		= Format[Byte]		(JsonNumber.fromByte,		in => numberValue(in).toByte)
	implicit lazy val ShortFormat		= Format[Short]		(JsonNumber.fromShort,		in => numberValue(in).toShort)
	implicit lazy val IntFormat			= Format[Int]		(JsonNumber.fromInt,		in => numberValue(in).toInt)
	implicit lazy val LongFormat		= Format[Long]		(JsonNumber.fromLong,		in => numberValue(in).toLong)
	implicit lazy val FloatFormat		= Format[Float]		(JsonNumber.fromFloat,		in => numberValue(in).toFloat)
	implicit lazy val DoubleFormat		= Format[Double]	(JsonNumber.fromDouble,		in => numberValue(in).toDouble)
	implicit lazy val BooleanFormat		= Format[Boolean]	(JsonBoolean(_),			in => booleanValue(in))
	implicit lazy val CharFormat		= Format[Char]		(out => JsonString(out.toString), in => stringValue(in).head )
	
	implicit lazy val BigIntFormat		= Format[BigInt]	(JsonNumber.fromBigInt, in => numberValue(in).toBigInt )
	implicit lazy val BigDecimalFormat 	= Format[BigDecimal](JsonNumber(_), in => numberValue(in) )
	
	implicit lazy val StringFormat		= Format[String]	(JsonString(_), in	=> stringValue(in) )
}
