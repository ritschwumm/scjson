package scjson

/** helper to allow easy construction and pattern matching on JSONArray instances */
object JSONVarArray {
	def apply(values:JSONValue*):JSONArray					= JSONArray(values)
	def unapplySeq(array:JSONArray):Option[Seq[JSONValue]]	= Some(array.value)
}

/** helper to allow easy construction and pattern matching on JSONObject instances */
object JSONVarObject {
	def apply(it:(String,JSONValue)*):JSONObject					= JSONObject(it)
	def unapplySeq(it:JSONObject):Option[Seq[(String,JSONValue)]]	= Some(it.value)
}
