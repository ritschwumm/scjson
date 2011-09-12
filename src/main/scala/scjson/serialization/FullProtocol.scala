package scjson.serialization

object FullProtocol extends FullProtocol

trait FullProtocol 
		extends	NativeProtocol 
		with	CollectionProtocol 
		with	TupleProtocol
		with	CaseClassProtocol
		with	JSValueProtocol
