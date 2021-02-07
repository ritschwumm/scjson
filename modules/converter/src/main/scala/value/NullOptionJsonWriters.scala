package scjson.converter

import scutil.lang._
import scjson.ast._

import scjson.converter.{
	CollectionConverters	=> CC,
	AltSeqConverters		=> ASC
}
trait NullOptionJsonWriters extends NullOptionJsonWritersLow with UnitJsonWriters {
	implicit def OptionOptionWriter[T](implicit ev:JsonWriter[Option[T]]):JsonWriter[Option[Option[T]]]	=
		CC.optionToEither[JsonError,Option[T]]	>=>
		ASC.altWriter("none", "some")

	/*
	// in old times, OptionWriter would have broken for Option[Unit] because Unit was encoded as json null just like None.
	implicit val OptionUnitWriter:JsonWriter[Option[Unit]]	=
		JC.makeBoolean contraMap {
			case Some(())	=> true
			case None		=> false
		}
	*/
}

trait NullOptionJsonWritersLow {
	implicit def OptionNullWriter[T:JsonWriter]:JsonWriter[Option[T]]	=
		Converter {
			case None		=> Validated valid JsonValue.Null
			case Some(x)	=> JsonWriter[T] convert x
		}
}
