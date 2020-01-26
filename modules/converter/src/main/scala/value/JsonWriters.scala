package scjson.converter

object JsonWriters extends JsonWriters

trait JsonWriters
	extends	PrimitiveJsonWriters
	with	UnitJsonWriters
	with	NewtypeJsonWriters
	with	TupleJsonWriters
	with	EitherJsonWriters
	with	NullOptionJsonWriters
	with	EnumJsonWriters
	with	SumJsonWriters
	with	CollectionJsonWriters
	with	CaseJsonWriters
	with	JsonWritersLow
	// has to be at the bottom, or implicit search diverges
	with	JsonIdentity

trait JsonWritersLow extends KeyJsonWriters
