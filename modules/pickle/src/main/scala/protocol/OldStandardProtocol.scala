package scjson.pickle.protocol

object OldStandardProtocol extends OldStandardProtocol

/**
uses OldOptionProtocol instead of NullOptionProtocol,
for new development StandardProtocol is preferred
*/
trait OldStandardProtocol
	extends	NativeProtocol
	with	ViaProtocol
	with	SeqProtocol
	with	CollectionProtocol
	with	OldOptionProtocol
	with	EitherProtocol
	//with	SumProtocol
	//with	ObjectSumProtocol
	with	EnumProtocol
	with	TupleProtocol
	with	CaseClassProtocol
	with	IdentityProtocol
