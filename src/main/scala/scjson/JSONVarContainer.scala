package scjson

import scutil.lang.ISeq

/** helper to allow easy construction and pattern matching on JSONArray instances */
object JSONVarArray {
	def apply(values:JSONValue*):JSONArray					= JSONArray(values.toVector)
	def unapplySeq(array:JSONArray):Option[ISeq[JSONValue]]	= Some(array.value)
}

/** helper to allow easy construction and pattern matching on JSONObject instances */
object JSONVarObject {
	def apply(it:(String,JSONValue)*):JSONObject					= JSONObject(it.toVector)
	def unapplySeq(it:JSONObject):Option[ISeq[(String,JSONValue)]]	= Some(it.value)
}
