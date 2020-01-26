package scjson.converter

import scjson.converter.{
	CollectionConverters	=> CC,
	AltSeqConverters		=> ASC
}

trait OldOptionJsonReaders extends UnitJsonReaders {
	// replaced by OptionOptionReader and OptionNullReader in NullOptionJsonReaders(Low)
	implicit def OldOptionReader[T:JsonReader]:JsonReader[Option[T]]	=
		ASC.altReader[Unit,T]("none", "some")	>=>
		CC.eitherToOption
}
