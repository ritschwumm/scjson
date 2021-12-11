package scjson.converter

import scutil.lang.*

import scjson.converter.{
	CollectionConverters	as CC,
	AltSeqConverters		as ASC
}

trait NullOptionJsonReaders extends NullOptionJsonReadersLow {
	private given NullOptionJsonUnitReader:JsonReader[Unit]	= UnitConverters.UnitReader

	given OptionOptionReader[T](using ev:JsonReader[Option[T]]):JsonReader[Option[Option[T]]]	=
		ASC.altReader[Unit,Option[T]]("none", "some")	>=>
		CC.eitherToOption

	/*
	// in old times, OptionReader would have broken for Option[Unit] because Unit was encoded as json null just like None.
	given OptionUnitReader:JsonReader[Option[Unit]]	=
		JC.expectBoolean	map {
			case true	=> Some(())
			case false	=> None
		}
	*/
}

trait NullOptionJsonReadersLow {
	given OptionNullReader[T:JsonReader]:JsonReader[Option[T]]	=
		Converter { it =>
			if (it.isNull)	Validated valid None
			else			JsonReader[T] convert it map Some.apply
		}
}
