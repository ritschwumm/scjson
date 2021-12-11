package scjson.converter

import scjson.converter.{
	CollectionConverters	=> CC,
	AltSeqConverters		=> ASC
}

trait OldOptionJsonReaders extends UnitJsonReaders {
	private given OldOptionJsonUnitReader:JsonReader[Unit]	= UnitConverters.UnitReader

	/** replaced by OptionOptionReader and OptionNullReader in NullOptionJsonReaders(Low) */
	given OldOptionReader[T:JsonReader]:JsonReader[Option[T]]	=
		ASC.altReader[Unit,T]("none", "some")	>=>
		CC.eitherToOption
}
