package scjson.ast

import scutil.base.implicits._
import scutil.lang.ISeq

object JsonValue {
	val theNull:JsonValue								= JsonNull
	def mkNull(it:Unit):JsonValue						= theNull
	val theTrue:JsonValue								= JsonBoolean(true)
	val theFalse:JsonValue								= JsonBoolean(false)
	def mkBoolean(it:Boolean):JsonValue					= JsonBoolean(it)
	def mkNumber(it:BigDecimal):JsonValue				= JsonNumber(it)
	def mkString(it:String):JsonValue					= JsonString(it)
	def mkArray(it:ISeq[JsonValue]):JsonValue			= JsonArray(it)
	def mkObject(it:ISeq[(String,JsonValue)]):JsonValue	= JsonObject(it)

	val emptyString:JsonValue	= JsonString("")
	val emptyArray:JsonValue	= JsonArray(List.empty)
	val emptyObject:JsonValue	= JsonObject(List.empty)

	def varArray(values:JsonValue*):JsonValue			= JsonArray(values.toVector)
	def varObject(values:(String,JsonValue)*):JsonValue	= JsonObject(values.toVector)
}

sealed abstract class JsonValue {
	def asNull:Option[Unit]							= this matchOption { case JsonNull			=> () }
	def asBoolean:Option[Boolean]					= this matchOption { case JsonBoolean(x)	=> x }
	def asNumber:Option[BigDecimal]					= this matchOption { case JsonNumber(x)		=> x }
	def asString:Option[String]						= this matchOption { case JsonString(x)		=> x }
	def asArray:Option[ISeq[JsonValue]]				= this matchOption { case JsonArray(x)		=> x }
	def asObject:Option[ISeq[(String,JsonValue)]]	= this matchOption { case JsonObject(x)		=> x }
}

//------------------------------------------------------------------------------

case object JsonNull extends JsonValue

//------------------------------------------------------------------------------

object JsonBoolean {
	def apply(value:Boolean):JsonBoolean			= if (value) JsonTrue else JsonFalse
	def unapply(value:JsonBoolean):Option[Boolean]	= Some(value.value)
}

sealed abstract class JsonBoolean extends JsonValue {
	def value:Boolean	=
			this match {
				case JsonTrue	=> true
				case JsonFalse	=> false
			}
}
case object JsonTrue	extends JsonBoolean
case object JsonFalse	extends JsonBoolean

//------------------------------------------------------------------------------

object JsonNumber {
	def fromByte(value:Byte):JsonNumber	= {
		JsonNumber(BigDecimal(value))
	}
	def fromShort(value:Short):JsonNumber	= {
		JsonNumber(BigDecimal(value))
	}
	def fromInt(value:Int):JsonNumber	= {
		JsonNumber(BigDecimal(value))
	}
	def fromLong(value:Long):JsonNumber	= {
		JsonNumber(BigDecimal(value))
	}
	def fromFloat(value:Float):JsonNumber	= {
		require(!value.isInfinity,	s"unexpected infinite value: ${value.toString}")
		require(!value.isNaN,		s"unexpected NaN value: ${value.toString}")
		JsonNumber(BigDecimal(value.toDouble))
	}
	def fromDouble(value:Double):JsonNumber	= {
		require(!value.isInfinity,	s"unexpected infinite value: ${value.toString}")
		require(!value.isNaN,		s"unexpected NaN value: ${value.toString}")
		JsonNumber(BigDecimal(value.toDouble))
	}
	def fromBigInt(value:BigInt):JsonNumber	= {
		require(value ne null,		s"unexpected null value")
		JsonNumber(BigDecimal(value))
	}
}

final case class JsonNumber(value:BigDecimal)	extends JsonValue {
	require(value ne null,	s"unexpected null value")
}

//------------------------------------------------------------------------------

object JsonString {
	val empty	= new JsonString("")

	def apply(value:String):JsonString	=
			if (value == "")	empty
			else				new JsonString(value)
}

final case class JsonString(value:String)		extends JsonValue {
	require(value ne null,	s"unexpected null value")
}

//------------------------------------------------------------------------------

object JsonArray {
	val empty	= new JsonArray(Vector.empty)

	def apply(value:ISeq[JsonValue]):JsonArray	=
			if (value.isEmpty)	empty
			else				new JsonArray(value)

	object Var {
		def apply(values:JsonValue*):JsonArray					= JsonArray(values.toVector)
		def unapplySeq(array:JsonArray):Option[ISeq[JsonValue]]	= Some(array.value)
	}
}

final case class JsonArray(value:ISeq[JsonValue])	extends JsonValue {
	require(value ne null,	s"unexpected null value")
	def get(index:Int):Option[JsonValue]	= value lift index
	def ++ (that:JsonArray):JsonArray		= JsonArray(this.value ++ that.value)
}

//------------------------------------------------------------------------------

object JsonObject {
	val empty	= new JsonObject(Vector.empty)

	def apply(value:ISeq[(String,JsonValue)]):JsonObject	=
			if (value.isEmpty)	empty
			else				new JsonObject(value)

	object Var {
		def apply(it:(String,JsonValue)*):JsonObject					= JsonObject(it.toVector)
		def unapplySeq(it:JsonObject):Option[ISeq[(String,JsonValue)]]	= Some(it.value)
	}
}

final case class JsonObject(value:ISeq[(String,JsonValue)])	extends JsonValue {
	require(value ne null,	s"unexpected null value")
	def get(key:String):Option[JsonValue]	= value collectFirst { case (k,v) if (k == key) => v }
	def ++ (that:JsonObject):JsonObject		= JsonObject(this.value ++ that.value)
	def valueMap:Map[String,JsonValue]		= value.toMap
}
