package scjson.reflection.scalasignature

case class Signature(entries:Seq[Entry]) {
	def deref(ref:Ref):Entry	= entries(ref.index)
}
