package scjson.ast

import scutil.lang.Subtype

object JSONSubtypes {
	val jsonNull	= Subtype.partial[JSONValue,JSONNull.type]	{ case x@JSONNull		=> x }
	val jsonBoolean	= Subtype.partial[JSONValue,JSONBoolean]	{ case x@JSONBoolean(_)	=> x }
	val jsonNumber	= Subtype.partial[JSONValue,JSONNumber]		{ case x@JSONNumber(_)	=> x }
	val jsonString	= Subtype.partial[JSONValue,JSONString]		{ case x@JSONString(_)	=> x }
	val jsonArray	= Subtype.partial[JSONValue,JSONArray]		{ case x@JSONArray(_)	=> x }
	val jsonObject	= Subtype.partial[JSONValue,JSONObject]		{ case x@JSONObject(_)	=> x }
}
