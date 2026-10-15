package scjson.converter

import scutil.lang.*

trait NullOptionJsonReaders extends NullOptionJsonReadersLow {
	private given NullOptionJsonUnitReader:JsonReader[Unit]	= UnitConverters.UnitReader

	given OptionOptionReader: [T] => (ev:JsonReader[Option[T]]) => JsonReader[Option[Option[T]]]	=
		AltSeqConverters.altReader[Unit,Option[T]]("none", "some")	>=>
		CollectionConverters.eitherToOption

	/*
	// in old times, OptionReader would have broken for Option[Unit] because Unit was encoded as json null just like None.
	given OptionUnitReader:JsonReader[Option[Unit]]	=
		JsonConverters.expectBoolean.map {
			case true	=> Some(())
			case false	=> None
		}
	*/
}

trait NullOptionJsonReadersLow {
	given OptionNullReader: [T:JsonReader] => JsonReader[Option[T]]	=
		Converter { it =>
			if it.isNull
			then Validated.valid(None)
			else JsonReader[T].convert(it).map(Some.apply)
		}
}
