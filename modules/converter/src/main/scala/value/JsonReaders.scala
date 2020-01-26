package scjson.converter

object JsonReaders extends JsonReaders

trait JsonReaders
	extends	PrimitiveJsonReaders
	with	UnitJsonReaders
	with	NewtypeJsonReaders
	with	TupleJsonReaders
	with	EitherJsonReaders
	with	NullOptionJsonReaders
	with	EnumJsonReaders
	with	CollectionJsonReaders
	with	CaseJsonReaders
	with	JsonReadersLow
	// has to be at the bottom, or implicit search diverges
	with	JsonIdentity

trait JsonReadersLow extends KeyJsonReaders
