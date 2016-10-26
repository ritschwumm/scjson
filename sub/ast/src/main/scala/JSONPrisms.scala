package scjson.ast

import scutil.lang._

object JSONPrisms {
	val nullPrism	:Prism[JSONValue,Unit]						= Prism(_.asNull,		JSONValue.mkNull)
	val booleanPrism:Prism[JSONValue,Boolean]					= Prism(_.asBoolean,	JSONValue.mkBoolean)
	val numberPrism	:Prism[JSONValue,BigDecimal]				= Prism(_.asNumber,		JSONValue.mkNumber)
	val stringPrism	:Prism[JSONValue,String]					= Prism(_.asString,		JSONValue.mkString)
	val arrayPrism	:Prism[JSONValue,ISeq[JSONValue]]			= Prism(_.asArray,		JSONValue.mkArray)
	val objectPrism	:Prism[JSONValue,ISeq[(String,JSONValue)]]	= Prism(_.asObject,		JSONValue.mkObject)
}
