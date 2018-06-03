package scjson.pickle.protocol

@deprecated("0.153.0", "use StandardProtocol2 without SumProtocol and ObjectSumProtocol")
object StandardProtocol extends StandardProtocol

@deprecated("0.153.0", "use StandardProtocol2 without SumProtocol and ObjectSumProtocol")
trait StandardProtocol
		extends	NativeProtocol
		with	ViaProtocol
		with	ISeqProtocol
		with	CollectionProtocol
		with	NullOptionProtocol
		with	EitherProtocol
		with	SumProtocol
		with	ObjectSumProtocol
		with	EnumProtocol
		with	TupleProtocol
		with	CaseClassProtocol
		with	IdentityProtocol
