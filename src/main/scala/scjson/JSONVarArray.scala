package scjson

/** helper to allow pattern matching on JSONArrays */
object JSONVarArray {
	def apply(values:JSONValue*):JSONArray					= JSONArray(values)
	def unapplySeq(array:JSONArray):Option[Seq[JSONValue]]	= Some(array.value)
}
