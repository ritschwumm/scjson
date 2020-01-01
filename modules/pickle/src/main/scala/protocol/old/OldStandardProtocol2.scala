package scjson.pickle.protocol.old

import scjson.pickle.protocol._

object OldStandardProtocol2 extends OldStandardProtocol2

/**
uses OldOptionProtocol instead of NullOptionProtocol,
for new development StandardProtocol is preferred
*/
trait OldStandardProtocol2
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
