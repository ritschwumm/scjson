package scjson.converter

import scutil.lang._

import scjson.converter.{
	CollectionConverters	=> CC,
	AltSeqConverters		=> ASC
}

trait NullOptionJsonReaders extends NullOptionJsonReadersLow {
	private implicit val NullOptionJsonUnitReader:JsonReader[Unit]	= UnitConverters.UnitReader

	implicit def OptionOptionReader[T](implicit ev:JsonReader[Option[T]]):JsonReader[Option[Option[T]]]	=
		ASC.altReader[Unit,Option[T]]("none", "some")	>=>
		CC.eitherToOption

	/*
	// in old times, OptionReader would have broken for Option[Unit] because Unit was encoded as json null just like None.
	implicit def OptionUnitReader:JsonReader[Option[Unit]]	=
		JC.expectBoolean	map {
			case true	=> Some(())
			case false	=> None
		}
	*/
}

trait NullOptionJsonReadersLow {
	implicit def OptionNullReader[T:JsonReader]:JsonReader[Option[T]]	=
		Converter { it =>
			if (it.isNull)	Validated valid None
			else			JsonReader[T] convert it map Some.apply
		}
}
