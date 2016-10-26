package scjson.pickle.protocol

import scutil.lang._

import scjson.ast._
import scjson.pickle._

import JSONPickleUtil._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit lazy val UnitFormat		= Format[Unit]		(constant(JSONObject.empty),	constant(()))
	implicit lazy val NullFormat		= Format[Null]		(constant(JSONNull),			constant(null))
	
	implicit lazy val ByteFormat		= Format[Byte]		(JSONNumber(_),		in => numberValue(in).toByte)
	implicit lazy val ShortFormat		= Format[Short]		(JSONNumber(_),		in => numberValue(in).toShort)
	implicit lazy val IntFormat			= Format[Int]		(JSONNumber(_),		in => numberValue(in).toInt)
	implicit lazy val LongFormat		= Format[Long]		(JSONNumber(_),		in => numberValue(in).toLong)
	implicit lazy val FloatFormat		= Format[Float]		(JSONNumber(_),		in => numberValue(in).toFloat)
	implicit lazy val DoubleFormat		= Format[Double]	(JSONNumber(_),		in => numberValue(in).toDouble)
	implicit lazy val BooleanFormat		= Format[Boolean]	(JSONBoolean(_),	in => booleanValue(in))
	implicit lazy val CharFormat		= Format[Char]		(out => JSONString(out.toString), in => stringValue(in).head )
	
	implicit lazy val BigIntFormat		= Format[BigInt]	(JSONNumber(_), in => numberValue(in).toBigInt )
	implicit lazy val BigDecimalFormat 	= Format[BigDecimal](JSONNumber(_), in => numberValue(in) )
	
	implicit lazy val StringFormat		= Format[String]	(JSONString(_), in	=> stringValue(in) )
}
