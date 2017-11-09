package scjson.ast

import scutil.lang._

object JSONPrisms {
	val nullRef		:Prism[JSONValue,Unit]						= Prism(_.asNull,		JSONValue.mkNull)
	val boolean		:Prism[JSONValue,Boolean]					= Prism(_.asBoolean,	JSONValue.mkBoolean)
	val number		:Prism[JSONValue,BigDecimal]				= Prism(_.asNumber,		JSONValue.mkNumber)
	val string		:Prism[JSONValue,String]					= Prism(_.asString,		JSONValue.mkString)
	val arraySeq	:Prism[JSONValue,ISeq[JSONValue]]			= Prism(_.asArray,		JSONValue.mkArray)
	val objectSeq	:Prism[JSONValue,ISeq[(String,JSONValue)]]	= Prism(_.asObject,		JSONValue.mkObject)
}
