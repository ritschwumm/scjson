package scjson.pickle.protocol

import scutil.lang._

import scjson.ast._
import scjson.pickle._

import JsonPickleUtil._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit lazy val UnitFormat		= Format[Unit]		(constant(JsonValue.emptyObject),	constant(()))
	implicit lazy val NullFormat		= Format[Null]		(constant(JsonValue.Null),			constant(null))

	implicit lazy val ByteFormat		= Format[Byte]		(JsonValue.fromByte,	in => numberValue(in).toByte)
	implicit lazy val ShortFormat		= Format[Short]		(JsonValue.fromShort,	in => numberValue(in).toShort)
	implicit lazy val IntFormat			= Format[Int]		(JsonValue.fromInt,		in => numberValue(in).toInt)
	implicit lazy val LongFormat		= Format[Long]		(JsonValue.fromLong,	in => numberValue(in).toLong)
	implicit lazy val FloatFormat		= Format[Float]		(JsonValue.fromFloat,	in => numberValue(in).toFloat)
	implicit lazy val DoubleFormat		= Format[Double]	(JsonValue.fromDouble,	in => numberValue(in).toDouble)
	implicit lazy val BooleanFormat		= Format[Boolean]	(JsonValue.fromBoolean,	in => booleanValue(in))
	implicit lazy val CharFormat		= Format[Char]		(out => JsonValue.fromString(out.toString),	in => stringValue(in).head )

	implicit lazy val BigIntFormat		= Format[BigInt]	(JsonValue.fromBigInt,		in => numberValue(in).toBigInt )
	implicit lazy val BigDecimalFormat 	= Format[BigDecimal](JsonValue.fromBigDecimal,	in => numberValue(in) )

	implicit lazy val StringFormat		= Format[String]	(JsonValue.fromString(_),	in	=> stringValue(in) )
}
