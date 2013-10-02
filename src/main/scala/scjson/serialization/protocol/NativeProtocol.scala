package scjson.serialization

import scutil.lang._

import scjson._

import JSONSerializationUtil._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit val UnitFormat	= Format[Unit](
		constant(JSONObject.empty),
		constant(())
	)
	
	implicit val NullFormat = Format[Null](
		constant(JSONNull),
		constant(null)
	)

	implicit val ByteFormat = Format[Byte](
		JSONNumber(_),
		in	=> numberValue(in).toByte
	)
	implicit val ShortFormat = Format[Short](
		JSONNumber(_),
		in	=> numberValue(in).toShort
	)
	implicit val IntFormat = Format[Int](
		JSONNumber(_),
		in	=> numberValue(in).toInt
	)
	implicit val LongFormat = Format[Long](
		JSONNumber(_),
		in	=> numberValue(in).toLong
	) 
	implicit val FloatFormat = Format[Float](
		JSONNumber(_),
		in	=> numberValue(in).toFloat
	)
	implicit val DoubleFormat = Format[Double](
		JSONNumber(_),
		in	=> numberValue(in).toDouble
	)
	
	implicit val BigIntFormat = Format[BigInt](
		JSONNumber(_),
		in	=> numberValue(in).toBigInt
	)
	implicit val BigDecimalFormat  = Format[BigDecimal](
		JSONNumber(_),
		in	=> numberValue(in)
	)
	
	implicit val BooleanFormat = Format[Boolean](
		JSONBoolean(_),
		in	=> booleanValue(in)
	)
	
	implicit val CharFormat = Format[Char](
		out => JSONString(out.toString),
		in	=> stringValue(in).head
	)
	implicit val StringFormat = Format[String](
		JSONString(_),
		in	=> stringValue(in)
	)
}
