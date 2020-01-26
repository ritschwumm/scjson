package scjson.converter

import scjson.converter.{
	CollectionConverters	=> CC,
	AltSeqConverters		=> ASC
}
trait OldOptionJsonWriters extends UnitJsonWriters {
	// replaced by OptionOptionWriter and OptionNullWriter in NullOptionJsonWriters(Low)
	implicit def OldOptionWriter[T:JsonWriter]:JsonWriter[Option[T]]	=
		CC.optionToEither[JsonError,T]	>=>
		ASC.altWriter("none", "some")
}
