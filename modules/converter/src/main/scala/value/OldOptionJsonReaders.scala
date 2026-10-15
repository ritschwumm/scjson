package scjson.converter

trait OldOptionJsonReaders {
	private given OldOptionJsonUnitReader:JsonReader[Unit]	= UnitConverters.UnitReader

	/** replaced by OptionOptionReader and OptionNullReader in NullOptionJsonReaders(Low) */
	given OldOptionReader: [T:JsonReader] => JsonReader[Option[T]]	=
		AltSeqConverters.altReader[Unit,T]("none", "some")	>=>
		CollectionConverters.eitherToOption
}
