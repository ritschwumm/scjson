package scjson

/** helper to allow pattern matching on JSArrays */
object JSVarArray {
	def apply(values:JSValue*):JSArray					= JSArray(values)
	def unapplySeq(array:JSArray):Option[Seq[JSValue]]	= Some(array.value)
}
