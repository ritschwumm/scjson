package scjson.ast

import scutil.base.implicits._
import scutil.lang.ISeq

object JsonValue {
	val theNull:JsonValue								= JsonNull
	def mkNull(it:Unit):JsonValue						= theNull
	def mkBoolean(it:Boolean):JsonValue					= JsonBoolean(it)
	def mkNumber(it:BigDecimal):JsonValue				= JsonNumber(it)
	def mkString(it:String):JsonValue					= JsonString(it)
	def mkArray(it:ISeq[JsonValue]):JsonValue			= JsonArray(it)
	def mkObject(it:ISeq[(String,JsonValue)]):JsonValue	= JsonObject(it)
}

sealed abstract class JsonValue {
	def asNull:Option[Unit]							= this matchOption { case JsonNull			=> () }
	def asBoolean:Option[Boolean]					= this matchOption { case JsonBoolean(x)	=> x }
	def asNumber:Option[BigDecimal]					= this matchOption { case JsonNumber(x)		=> x }
	def asString:Option[String]						= this matchOption { case JsonString(x)		=> x }
	def asArray:Option[ISeq[JsonValue]]				= this matchOption { case JsonArray(x)		=> x }
	def asObject:Option[ISeq[(String,JsonValue)]]	= this matchOption { case JsonObject(x)		=> x }
}

case object JsonNull extends JsonValue

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
		require(!value.isInfinity)
		require(!value.isNaN)
		JsonNumber(BigDecimal(value.toDouble))
	}
	def fromDouble(value:Double):JsonNumber	= {
		require(!value.isInfinity)
		require(!value.isNaN)
		JsonNumber(BigDecimal(value.toDouble))
	}
	def fromBigInt(value:BigInt):JsonNumber	= {
		require(value ne null)
		JsonNumber(BigDecimal(value))
	}
}
final case class JsonNumber(value:BigDecimal)	extends JsonValue {
	require(value ne null)
}

object JsonString {
	val empty	= JsonString("")
}
final case class JsonString(value:String)		extends JsonValue {
	require(value ne null)
}

object JsonArray {
	val empty	= JsonArray(Vector.empty)

	object Var {
		def apply(values:JsonValue*):JsonArray					= JsonArray(values.toVector)
		def unapplySeq(array:JsonArray):Option[ISeq[JsonValue]]	= Some(array.value)
	}
}
final case class JsonArray(value:ISeq[JsonValue])	extends JsonValue {
	require(value ne null)
	def get(index:Int):Option[JsonValue]	= value lift index
	def ++ (that:JsonArray):JsonArray		= JsonArray(this.value ++ that.value)
}

object JsonObject {
	val empty	= JsonObject(Vector.empty)

	object Var {
		def apply(it:(String,JsonValue)*):JsonObject					= JsonObject(it.toVector)
		def unapplySeq(it:JsonObject):Option[ISeq[(String,JsonValue)]]	= Some(it.value)
	}
}
final case class JsonObject(value:ISeq[(String,JsonValue)])	extends JsonValue {
	require(value ne null)
	def get(key:String):Option[JsonValue]	= value collectFirst { case (k,v) if (k == key) => v }
	def ++ (that:JsonObject):JsonObject		= JsonObject(this.value ++ that.value)
	def valueMap:Map[String,JsonValue]		= value.toMap
}
