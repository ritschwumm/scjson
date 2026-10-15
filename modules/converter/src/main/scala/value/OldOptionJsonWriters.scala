package scjson.converter

trait OldOptionJsonWriters {
	private given OldOptionJsonUnitWriter:JsonWriter[Unit]	= UnitConverters.UnitWriter

	/** replaced by OptionOptionWriter and OptionNullWriter in NullOptionJsonWriters(Low) */
	given OldOptionWriter: [T:JsonWriter] => JsonWriter[Option[T]]	=
		CollectionConverters.optionToEither[JsonError, T]	>=>
		AltSeqConverters.altWriter("none", "some")
}
