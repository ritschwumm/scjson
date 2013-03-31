package scjson.serialization

object FullProtocol extends FullProtocol

trait FullProtocol 
		extends	NativeProtocol 
		with	ViaProtocol
		with	CollectionProtocol 
		with	TupleProtocol
		with	SumProtocol
		with	ObjectSumProtocol
		with	EnumProtocol
		with	CaseClassProtocol
		with	IdentityProtocol
