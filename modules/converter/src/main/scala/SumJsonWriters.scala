package scjson.converter

import scutil.lang._
import scjson.ast._

import scjson.converter.{
	AltSeqConverters	=> ASC
}

trait SumJsonWriters extends SumJsonWritersLow with UnitJsonWriters {
	implicit def EitherWriter[T1:JsonWriter,T2:JsonWriter]:JsonWriter[Either[T1,T2]]	=
		ASC altWriter ("left", "right")

	/*
	// replaced by OptionOptionWriter and OptionWriter in JsonWritersLow
	implicit def OptionWriter[T:JsonWriter]:JsonWriter[Option[T]]	=
		optionToEither[JsonError,T]	>=>
		ASC.altWriter("none", "some")
	*/

	implicit def OptionOptionWriter[T](implicit ev:JsonWriter[Option[T]]):JsonWriter[Option[Option[T]]]	=
		optionToEither[JsonError,Option[T]]	>=>
		ASC.altWriter("none", "some")

	/*
	// in old times, OptionWriter would have broken for Option[Unit] because Unit was encoded as JsonNull just like None.
	implicit val OptionUnitWriter:JsonWriter[Option[Unit]]	=
		JC.makeBoolean contraMap {
			case Some(())	=> true
			case None		=> false
		}
	*/

	//------------------------------------------------------------------------------

	private def optionToEither[E,T]:Converter[E,Option[T],Either[Unit,T]]	=
		Converter total (_.fold[Either[Unit,T]](Left(()))(Right(_)))
}

trait SumJsonWritersLow {
	implicit def OptionWriter[T:JsonWriter]:JsonWriter[Option[T]]	=
		Converter {
			case None		=> Validated good JsonNull
			case Some(x)	=> implicitly[JsonWriter[T]] apply x
		}
}
