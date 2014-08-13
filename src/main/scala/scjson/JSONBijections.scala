package scjson

import scutil.lang._

object JSONBijections {
	val jsonNull		= Bijection[JSONNull.type,Null]						(_ => null, _ => JSONNull)
	val jsonBoolean		= Bijection[JSONBoolean,Boolean]					(_.value,	JSONBoolean(_))
	val jsonNumber		= Bijection[JSONNumber,BigDecimal]					(_.value,	JSONNumber(_))
	val jsonString		= Bijection[JSONString,String]						(_.value,	JSONString(_))
	val jsonArray		= Bijection[JSONArray,ISeq[JSONValue]]				(_.value,	JSONArray(_))
	val jsonObject		= Bijection[JSONObject,ISeq[(String,JSONValue)]]		(_.value,	JSONObject(_))
}
