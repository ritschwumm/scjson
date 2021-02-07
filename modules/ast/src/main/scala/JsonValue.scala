package scjson.ast

import scutil.core.implicits._

object JsonValue {
	val Null:JsonValue	= JsonNull

	//------------------------------------------------------------------------------

	val True:JsonValue						= JsonTrue
	val False:JsonValue						= JsonFalse

	def fromBoolean(it:Boolean):JsonValue	=
		if (it)	True
		else	False

	//------------------------------------------------------------------------------

	val Zero:JsonValue	= JsonNumber(0)

	def fromByte(value:Byte):JsonValue		= JsonNumber(BigDecimal(value))

	def fromShort(value:Short):JsonValue	= JsonNumber(BigDecimal(value))

	def fromInt(value:Int):JsonValue		= JsonNumber(BigDecimal(value))

	def fromLong(value:Long):JsonValue		= JsonNumber(BigDecimal(value))

	def fromFloat(value:Float):JsonValue	= {
		require(!value.isInfinity,	s"unexpected infinite value: ${value.toString}")
		require(!value.isNaN,		s"unexpected NaN value: ${value.toString}")
		JsonNumber(BigDecimal(value.toDouble))
	}

	def fromDouble(value:Double):JsonValue	= {
		require(!value.isInfinity,	s"unexpected infinite value: ${value.toString}")
		require(!value.isNaN,		s"unexpected NaN value: ${value.toString}")
		JsonNumber(BigDecimal(value.toDouble))
	}

	def fromBigInt(value:BigInt):JsonValue	= {
		require(value ne null,		s"unexpected null value")
		JsonNumber(BigDecimal(value))
	}

	def fromBigDecimal(value:BigDecimal):JsonValue	= {
		require(value ne null,		s"unexpected null value")
		//if (value != BigDecimal.ZERO)	JsonNumber(value) else Zero
		JsonNumber(value)
	}

	//------------------------------------------------------------------------------

	val emptyString:JsonValue	= JsonString("")

	def fromString(it:String):JsonValue		= {
		require(it ne null,	s"unexpected null value")
		if (it.nonEmpty)	JsonString(it)
		else				emptyString
	}

	//------------------------------------------------------------------------------

	val emptyArray:JsonValue	= JsonArray(List.empty)

	def fromItems(it:Seq[JsonValue]):JsonValue	= {
		require(it ne null,	s"unexpected null value")
		if (it.nonEmpty)	JsonArray(it)
		else				emptyArray
	}

	def arr(values:JsonValue*):JsonValue	= fromItems(values.toVector)

	//------------------------------------------------------------------------------

	val emptyObject:JsonValue	= JsonObject(List.empty)

	def fromFields(it:Seq[(String,JsonValue)]):JsonValue	= {
		require(it ne null,	s"unexpected null value")
		if (it.nonEmpty)	JsonObject(it)
		else				emptyObject
	}

	def obj(values:(String,JsonValue)*):JsonValue	= fromFields(values.toVector)

	//------------------------------------------------------------------------------

	private case object JsonNull										extends JsonValue
	private case object JsonTrue										extends JsonValue
	private case object JsonFalse										extends JsonValue
	private final case class JsonNumber(value:BigDecimal)				extends JsonValue
	private final case class JsonString(value:String)					extends JsonValue
	private final case class JsonArray(value:Seq[JsonValue])			extends JsonValue
	private final case class JsonObject(value:Seq[(String,JsonValue)])	extends JsonValue
}

sealed abstract class JsonValue {
	def isNull:Boolean	=
		this match {
			case JsonValue.JsonNull		=> true
			case _						=> false
		}

	def isTrue:Boolean	=
		this match {
			case JsonValue.JsonTrue		=> true
			case _						=> false
		}

	def isFalse:Boolean	=
		this match {
			case JsonValue.JsonFalse	=> true
			case _						=> false
		}

	//------------------------------------------------------------------------------

	def asNull:Option[Unit]			=
		this matchOption {
			case JsonValue.JsonNull			=> ()
		}

	def asBoolean:Option[Boolean]	=
		this matchOption {
			case JsonValue.JsonTrue			=> true
			case JsonValue.JsonFalse		=> false
		}

	def asNumber:Option[BigDecimal]	=
		this matchOption {
			case JsonValue.JsonNumber(x)	=> x
		}

	def asString:Option[String]	=
		this matchOption {
			case JsonValue.JsonString(x)	=> x
		}

	def asArray:Option[Seq[JsonValue]]	=
		this matchOption {
			case JsonValue.JsonArray(x)		=> x
		}

	def asObject:Option[Seq[(String,JsonValue)]]	=
		this matchOption {
			case JsonValue.JsonObject(x)	=> x
		}

	//------------------------------------------------------------------------------

	def modifyBoolean(func:Boolean=>Boolean)	=
		this match {
			case JsonValue.JsonFalse	=> JsonValue.fromBoolean(func(false))
			case JsonValue.JsonTrue		=> JsonValue.fromBoolean(func(true))
			case x						=> x
		}

	def modifyNumber(func:BigDecimal=>BigDecimal)	=
		this match {
			case JsonValue.JsonNumber(x)	=> JsonValue.fromBigDecimal(func(x))
			case x							=> x
		}

	def modifyString(func:String=>String)	=
		this match {
			case JsonValue.JsonString(x)	=> JsonValue.fromString(func(x))
			case x							=> x
		}

	def modifyArray(func:Seq[JsonValue]=>Seq[JsonValue])	=
		this match {
			case JsonValue.JsonArray(x)	=> JsonValue.fromItems(func(x))
			case x						=> x
		}

	def modifyObject(func:Seq[(String,JsonValue)]=>Seq[(String,JsonValue)])	=
		this match {
			case JsonValue.JsonObject(x)	=> JsonValue.fromFields(func(x))
			case x							=> x
		}

	//------------------------------------------------------------------------------

	def cata[T](
		onNull:()=>T,
		onBoolean:Boolean=>T,
		onNumber:BigDecimal=>T,
		onString:String=>T,
		onArray:Seq[JsonValue]=>T,
		onObject:Seq[(String, JsonValue)]=>T
	):T	=
		this match {
			case JsonValue.JsonNull			=> onNull()
			case JsonValue.JsonTrue			=> onBoolean(true)
			case JsonValue.JsonFalse		=> onBoolean(false)
			case JsonValue.JsonNumber(x)	=> onNumber(x)
			case JsonValue.JsonString(x)	=> onString(x)
			case JsonValue.JsonArray(x)		=> onArray(x)
			case JsonValue.JsonObject(x)	=> onObject(x)
		}

	def visit[T](visitor:JsonVisitor[T]):T	=
		this match {
			case JsonValue.JsonNull			=> visitor.onNull()
			case JsonValue.JsonTrue			=> visitor.onBoolean(true)
			case JsonValue.JsonFalse		=> visitor.onBoolean(false)
			case JsonValue.JsonNumber(x)	=> visitor.onNumber(x)
			case JsonValue.JsonString(x)	=> visitor.onString(x)
			case JsonValue.JsonArray(x)		=> visitor.onArray(x)
			case JsonValue.JsonObject(x)	=> visitor.onObject(x)
		}
}
