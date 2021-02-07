package scjson.ast

import scutil.core.implicits._

object JsonValue {
	@deprecated("use JsonValue.Null", "0.226.0")
	val theNull:JsonValue								= JsonNull
	@deprecated("use _ => JsonValue.Null", "0.226.0")
	def mkNull(it:Unit):JsonValue						= Null

	@deprecated("use JsonValue.True", "0.226.0")
	val theTrue:JsonValue								= JsonBoolean(true)
	@deprecated("use JsonValue.False", "0.226.0")
	val theFalse:JsonValue								= JsonBoolean(false)

	@deprecated("use JsonValue.fromBoolean", "0.226.0")
	def mkBoolean(it:Boolean):JsonValue					= JsonBoolean(it)
	@deprecated("use JsonValue.fromBoolean", "0.226.0")
	def mkNumber(it:BigDecimal):JsonValue				= JsonNumber(it)
	@deprecated("use JsonValue.fromString", "0.226.0")
	def mkString(it:String):JsonValue					= JsonString(it)
	@deprecated("use JsonValue.fromItems", "0.226.0")
	def mkArray(it:Seq[JsonValue]):JsonValue			= JsonArray(it)
	@deprecated("use JsonValue.fromFields", "0.226.0")
	def mkObject(it:Seq[(String,JsonValue)]):JsonValue	= JsonObject(it)

	//------------------------------------------------------------------------------

	val Null:JsonValue	= JsonNull

	def fromBoolean(it:Boolean):JsonValue	= if (it) JsonTrue else JsonFalse
	val True:JsonValue						= JsonTrue
	val False:JsonValue						= JsonFalse

	def fromByte(value:Byte):JsonValue			= fromBigDecimal(BigDecimal(value))
	def fromShort(value:Short):JsonValue		= fromBigDecimal(BigDecimal(value))
	def fromInt(value:Int):JsonValue			= fromBigDecimal(BigDecimal(value))
	def fromLong(value:Long):JsonValue			= fromBigDecimal(BigDecimal(value))
	def fromFloat(value:Float):JsonValue	= {
		require(!value.isInfinity,	s"unexpected infinite value: ${value.toString}")
		require(!value.isNaN,		s"unexpected NaN value: ${value.toString}")
		fromBigDecimal(BigDecimal(value.toDouble))
	}
	def fromDouble(value:Double):JsonValue	= {
		require(!value.isInfinity,	s"unexpected infinite value: ${value.toString}")
		require(!value.isNaN,		s"unexpected NaN value: ${value.toString}")
		fromBigDecimal(BigDecimal(value.toDouble))
	}
	def fromBigInt(value:BigInt):JsonValue	= {
		require(value ne null,		s"unexpected null value")
		fromBigDecimal(BigDecimal(value))
	}
	def fromBigDecimal(it:BigDecimal):JsonValue	= JsonNumber(it)
	val Zero:JsonValue	= JsonNumber(0)

	def fromString(it:String):JsonValue		= JsonString(it)
	val emptyString:JsonValue				= JsonString("")

	def fromItems(it:Seq[JsonValue]):JsonValue	= JsonArray(it)
	def arr(values:JsonValue*):JsonValue		= JsonArray(values.toVector)
	val emptyArray:JsonValue					= JsonArray(List.empty)

	def fromFields(it:Seq[(String,JsonValue)]):JsonValue	= JsonObject(it)
	def obj(values:(String,JsonValue)*):JsonValue			= JsonObject(values.toVector)
	val emptyObject:JsonValue								= JsonObject(List.empty)
}

sealed abstract class JsonValue {
	def asNull:Option[Unit]							= this matchOption { case JsonNull			=> () }
	def asBoolean:Option[Boolean]					= this matchOption { case JsonBoolean(x)	=> x }
	def asNumber:Option[BigDecimal]					= this matchOption { case JsonNumber(x)		=> x }
	def asString:Option[String]						= this matchOption { case JsonString(x)		=> x }
	def asArray:Option[Seq[JsonValue]]				= this matchOption { case JsonArray(x)		=> x }
	def asObject:Option[Seq[(String,JsonValue)]]	= this matchOption { case JsonObject(x)		=> x }

	def modifyBoolean(func:Boolean=>Boolean)	=
		this match {
			case JsonBoolean(x)	=> JsonBoolean(func(x))
			case x				=> x
		}

	def modifyNumber(func:BigDecimal=>BigDecimal)	=
		this match {
			case JsonNumber(x)	=> JsonNumber(func(x))
			case x				=> x
		}

	def modifyString(func:String=>String)	=
		this match {
			case JsonString(x)	=> JsonString(func(x))
			case x				=> x
		}

	def modifyArray(func:Seq[JsonValue]=>Seq[JsonValue])	=
		this match {
			case JsonArray(x)	=> JsonArray(func(x))
			case x				=> x
		}

	def modifyObject(func:Seq[(String,JsonValue)]=>Seq[(String,JsonValue)])	=
		this match {
			case JsonObject(x)	=> JsonObject(func(x))
			case x				=> x
		}

	def cata[T](
		onNull:()=>T,
		onBoolean:Boolean=>T,
		onNumber:BigDecimal=>T,
		onString:String=>T,
		onArray:Seq[JsonValue]=>T,
		onObject:Seq[(String, JsonValue)]=>T
	):T	=
		this match {
			case JsonNull		=> onNull()
			case JsonTrue		=> onBoolean(true)
			case JsonFalse		=> onBoolean(false)
			case JsonNumber(x)	=> onNumber(x)
			case JsonString(x)	=> onString(x)
			case JsonArray(x)	=> onArray(x)
			case JsonObject(x)	=> onObject(x)
		}

	def visit[T](visitor:JsonVisitor[T]):T	=
		this match {
			case JsonNull		=> visitor.onNull()
			case JsonTrue		=> visitor.onBoolean(true)
			case JsonFalse		=> visitor.onBoolean(false)
			case JsonNumber(x)	=> visitor.onNumber(x)
			case JsonString(x)	=> visitor.onString(x)
			case JsonArray(x)	=> visitor.onArray(x)
			case JsonObject(x)	=> visitor.onObject(x)
		}
}

//------------------------------------------------------------------------------

case object JsonNull extends JsonValue

//------------------------------------------------------------------------------

object JsonBoolean {
	private[scjson] def apply(value:Boolean):JsonBoolean	=
		if (value) JsonTrue else JsonFalse

	def unapply(value:JsonBoolean):Some[Boolean]	=
		Some(value == JsonTrue)
}

sealed abstract class JsonBoolean extends JsonValue {
	@deprecated("will be removed", "0.226.0")
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
	@deprecated("use JsonValue.fromByte", "0.226.0")
	def fromByte(value:Byte):JsonNumber	= {
		JsonNumber(BigDecimal(value))
	}
	@deprecated("use JsonValue.fromShort", "0.226.0")
	def fromShort(value:Short):JsonNumber	= {
		JsonNumber(BigDecimal(value))
	}
	@deprecated("use JsonValue.fromInt", "0.226.0")
	def fromInt(value:Int):JsonNumber	= {
		JsonNumber(BigDecimal(value))
	}
	@deprecated("use JsonValue.fromLong", "0.226.0")
	def fromLong(value:Long):JsonNumber	= {
		JsonNumber(BigDecimal(value))
	}
	@deprecated("use JsonValue.fromFloat", "0.226.0")
	def fromFloat(value:Float):JsonNumber	= {
		require(!value.isInfinity,	s"unexpected infinite value: ${value.toString}")
		require(!value.isNaN,		s"unexpected NaN value: ${value.toString}")
		JsonNumber(BigDecimal(value.toDouble))
	}
	@deprecated("use JsonValue.fromDouble", "0.226.0")
	def fromDouble(value:Double):JsonNumber	= {
		require(!value.isInfinity,	s"unexpected infinite value: ${value.toString}")
		require(!value.isNaN,		s"unexpected NaN value: ${value.toString}")
		JsonNumber(BigDecimal(value.toDouble))
	}
	@deprecated("use JsonValue.fromBigInt", "0.226.0")
	def fromBigInt(value:BigInt):JsonNumber	= {
		require(value ne null,		s"unexpected null value")
		JsonNumber(BigDecimal(value))
	}

	private[scjson] def apply(value:BigDecimal):JsonNumber	=
		new JsonNumber(value)
}

final case class JsonNumber(value:BigDecimal)	extends JsonValue {
	require(value ne null,	s"unexpected null value")
}

//------------------------------------------------------------------------------

object JsonString {
	private val empty	= new JsonString("")

	private[scjson] def apply(value:String):JsonString	=
		if (value == "")	empty
		else				new JsonString(value)
}

final case class JsonString(value:String)		extends JsonValue {
	require(value ne null,	s"unexpected null value")
}

//------------------------------------------------------------------------------

object JsonArray {
	private val empty	= new JsonArray(Vector.empty)

	private[scjson] def apply(value:Seq[JsonValue]):JsonArray	=
		if (value.isEmpty)	empty
		else				new JsonArray(value)

	@deprecated("use JsonValue.arr", "0.226.0")
	object Var {
		def apply(values:JsonValue*):JsonArray					= JsonArray(values.toVector)
		def unapplySeq(array:JsonArray):Some[Seq[JsonValue]]	= Some(array.value)
	}
}

final case class JsonArray(value:Seq[JsonValue])	extends JsonValue {
	require(value ne null,	s"unexpected null value")
	@deprecated("will be removed", "0.226.0")
	def get(index:Int):Option[JsonValue]	= value lift index
	@deprecated("will be removed", "0.226.0")
	def ++ (that:JsonArray):JsonArray		= JsonArray(this.value ++ that.value)
}

//------------------------------------------------------------------------------

object JsonObject {
	private val empty	= new JsonObject(Vector.empty)

	private[scjson] def apply(value:Seq[(String,JsonValue)]):JsonObject	=
		if (value.isEmpty)	empty
		else				new JsonObject(value)

	@deprecated("use JsonValue.obj", "0.226.0")
	object Var {
		def apply(it:(String,JsonValue)*):JsonObject				= JsonObject(it.toVector)
		def unapplySeq(it:JsonObject):Some[Seq[(String,JsonValue)]]	= Some(it.value)
	}
}

final case class JsonObject(value:Seq[(String,JsonValue)])	extends JsonValue {
	require(value ne null,	s"unexpected null value")
	@deprecated("will be removed", "0.226.0")
	def get(key:String):Option[JsonValue]	= value collectFirst { case (k,v) if (k == key) => v }
	@deprecated("will be removed", "0.226.0")
	def ++ (that:JsonObject):JsonObject		= JsonObject(this.value ++ that.value)
	@deprecated("will be removed", "0.226.0")
	def valueMap:Map[String,JsonValue]		= value.toMap
}
