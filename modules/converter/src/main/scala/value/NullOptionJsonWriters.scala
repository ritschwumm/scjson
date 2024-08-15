package scjson.converter

import scutil.lang.*
import scjson.ast.*

import scjson.converter.{
	CollectionConverters	as CC,
	AltSeqConverters		as ASC
}
trait NullOptionJsonWriters extends NullOptionJsonWritersLow {
	private given NullOptionJsonUnitWriter:JsonWriter[Unit]	= UnitConverters.UnitWriter

	given OptionOptionWriter[T](using ev:JsonWriter[Option[T]]):JsonWriter[Option[Option[T]]]	=
		CC.optionToEither[JsonError,Option[T]]	>=>
		ASC.altWriter("none", "some")

	/*
	// in old times, OptionWriter would have broken for Option[Unit] because Unit was encoded as json null just like None.
	given OptionUnitWriter:JsonWriter[Option[Unit]]	=
		JC.makeBoolean.contraMap {
			case Some(())	=> true
			case None		=> false
		}
	*/
}

trait NullOptionJsonWritersLow {
	given OptionNullWriter[T:JsonWriter]:JsonWriter[Option[T]]	=
		Converter {
			case None		=> Validated.valid(JsonValue.Null)
			case Some(x)	=> JsonWriter[T].convert(x)
		}
}
