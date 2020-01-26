package scjson.pickle.protocol

object StandardProtocol extends StandardProtocol

trait StandardProtocol
	extends	NativeProtocol
	with	ViaProtocol
	with	SeqProtocol
	with	CollectionProtocol
	with	NullOptionProtocol
	with	EitherProtocol
	//with	SumProtocol
	//with	ObjectSumProtocol
	with	EnumProtocol
	with	TupleProtocol
	with	CaseClassProtocol
	with	IdentityProtocol
