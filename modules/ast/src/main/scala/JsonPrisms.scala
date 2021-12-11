package scjson.ast

import scutil.lang.*

object JsonPrisms {
	val nullRef		:Prism[JsonValue,Unit]						= Prism(_.asNull,		_ => JsonValue.Null)
	val boolean		:Prism[JsonValue,Boolean]					= Prism(_.asBoolean,	JsonValue.fromBoolean)
	val number		:Prism[JsonValue,BigDecimal]				= Prism(_.asNumber,		JsonValue.fromBigDecimal)
	val string		:Prism[JsonValue,String]					= Prism(_.asString,		JsonValue.fromString)
	val arraySeq	:Prism[JsonValue,Seq[JsonValue]]			= Prism(_.asArray,		JsonValue.fromItems)
	val objectSeq	:Prism[JsonValue,Seq[(String,JsonValue)]]	= Prism(_.asObject,		JsonValue.fromFields)
}
