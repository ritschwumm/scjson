package scjson.pickle.protocol

object StandardProtocol2 extends StandardProtocol2

trait StandardProtocol2
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
