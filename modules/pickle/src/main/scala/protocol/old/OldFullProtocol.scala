package scjson.pickle.protocol.old

import scjson.pickle.protocol._

@deprecated("0.153.0", "use OldStandardProtocol2 without SumProtocol and ObjectSumProtocol")
object OldFullProtocol extends OldFullProtocol

/**
uses OldOptionProtocol instead of NullOptionProtocol,
for new development StandardProtocol is preferred
*/
@deprecated("0.153.0", "use OldStandardProtocol2 without SumProtocol and ObjectSumProtocol")
trait OldFullProtocol
		extends	NativeProtocol
		with	ViaProtocol
		with	ISeqProtocol
		with	CollectionProtocol
		with	OldOptionProtocol
		with	EitherProtocol
		with	SumProtocol
		with	ObjectSumProtocol
		with	EnumProtocol
		with	TupleProtocol
		with	CaseClassProtocol
		with	IdentityProtocol
