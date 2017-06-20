package scjson.pickle.protocol.old

import scjson.pickle.protocol._

object OldFullProtocol extends OldFullProtocol

/**
uses OldOptionProtocol instead of NullOptionProtocol,
for new development StandardProtocol is preferred
*/
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
