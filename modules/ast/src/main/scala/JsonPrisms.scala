package scjson.ast

import scutil.lang._

object JsonPrisms {
	val nullRef		:Prism[JsonValue,Unit]						= Prism(_.asNull,		JsonValue.mkNull)
	val boolean		:Prism[JsonValue,Boolean]					= Prism(_.asBoolean,	JsonValue.mkBoolean)
	val number		:Prism[JsonValue,BigDecimal]				= Prism(_.asNumber,		JsonValue.mkNumber)
	val string		:Prism[JsonValue,String]					= Prism(_.asString,		JsonValue.mkString)
	val arraySeq	:Prism[JsonValue,Seq[JsonValue]]			= Prism(_.asArray,		JsonValue.mkArray)
	val objectSeq	:Prism[JsonValue,Seq[(String,JsonValue)]]	= Prism(_.asObject,		JsonValue.mkObject)
}
