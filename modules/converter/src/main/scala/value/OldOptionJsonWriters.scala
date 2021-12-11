package scjson.converter

import scjson.converter.{
	CollectionConverters	as CC,
	AltSeqConverters		as ASC
}
trait OldOptionJsonWriters {
	private given OldOptionJsonUnitWriter:JsonWriter[Unit]	= UnitConverters.UnitWriter

	/** replaced by OptionOptionWriter and OptionNullWriter in NullOptionJsonWriters(Low) */
	given OldOptionWriter[T:JsonWriter]:JsonWriter[Option[T]]	=
		CC.optionToEither[JsonError,T]	>=>
		ASC.altWriter("none", "some")
}
