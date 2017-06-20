package scjson.ast

import scutil.base.implicits._
import scutil.lang.ISeq

object JSONValue {
	val theNull:JSONValue								= JSONNull
	def mkNull(it:Unit):JSONValue						= JSONNull
	def mkBoolean(it:Boolean):JSONValue					= JSONBoolean(it)
	def mkNumber(it:BigDecimal):JSONValue				= JSONNumber(it)
	def mkString(it:String):JSONValue					= JSONString(it)
	def mkArray(it:ISeq[JSONValue]):JSONValue			= JSONArray(it)
	def mkObject(it:ISeq[(String,JSONValue)]):JSONValue	= JSONObject(it)
}

sealed abstract class JSONValue {
	def asNull:Option[Unit]							= this matchOption { case JSONNull			=> () }
	def asBoolean:Option[Boolean]					= this matchOption { case JSONBoolean(x)	=> x }
	def asNumber:Option[BigDecimal]					= this matchOption { case JSONNumber(x)		=> x }
	def asString:Option[String]						= this matchOption { case JSONString(x)		=> x }
	def asArray:Option[ISeq[JSONValue]]				= this matchOption { case JSONArray(x)		=> x }
	def asObject:Option[ISeq[(String,JSONValue)]]	= this matchOption { case JSONObject(x)		=> x }
}

case object JSONNull extends JSONValue

object JSONBoolean {
	def apply(value:Boolean):JSONBoolean			= if (value) JSONTrue else JSONFalse
	def unapply(value:JSONBoolean):Option[Boolean]	= Some(value.value)
}
sealed abstract class JSONBoolean extends JSONValue {
	def value:Boolean	=
			this match {
				case JSONTrue	=> true
				case JSONFalse	=> false
			}
}
case object JSONTrue	extends JSONBoolean
case object JSONFalse	extends JSONBoolean

object JSONNumber {
	def fromByte(value:Byte):JSONNumber	= {
		JSONNumber(BigDecimal(value))
	}
	def fromShort(value:Short):JSONNumber	= {
		JSONNumber(BigDecimal(value))
	}
	def fromInt(value:Int):JSONNumber	= {
		JSONNumber(BigDecimal(value))
	}
	def fromLong(value:Long):JSONNumber	= {
		JSONNumber(BigDecimal(value))
	}
	def fromFloat(value:Float):JSONNumber	= {
		require(!value.isInfinity)
		require(!value.isNaN)
		JSONNumber(BigDecimal(value.toDouble))
	}
	def fromDouble(value:Double):JSONNumber	= {
		require(!value.isInfinity)
		require(!value.isNaN)
		JSONNumber(BigDecimal(value.toDouble))
	}
	def fromBigInt(value:BigInt):JSONNumber	= {
		require(value ne null)
		JSONNumber(BigDecimal(value))
	}
}
final case class JSONNumber(value:BigDecimal)	extends JSONValue {
	require(value ne null)
}

final case class JSONString(value:String)		extends JSONValue {
	require(value ne null)
}

object JSONArray {
	val empty	= JSONVarArray()
}
final case class JSONArray(value:ISeq[JSONValue])	extends JSONValue {
	require(value ne null)
	def get(index:Int):Option[JSONValue]	= value lift index
	def ++ (that:JSONArray):JSONArray		= JSONArray(this.value ++ that.value)
}

object JSONObject {
	val empty	= JSONObject(ISeq.empty)
}
final case class JSONObject(value:ISeq[(String,JSONValue)])	extends JSONValue {
	require(value ne null)
	def get(key:String):Option[JSONValue]	= value collectFirst { case (k,v) if (k == key) => v }
	def ++ (that:JSONObject):JSONObject		= JSONObject(this.value ++ that.value)
	def valueMap:Map[String,JSONValue]		= value.toMap
}
