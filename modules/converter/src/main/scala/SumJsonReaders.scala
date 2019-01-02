package scjson.converter

import scutil.lang._
import scjson.ast._

import scjson.converter.{
	AltSeqConverters	=> ASC
}

trait SumJsonReaders extends SumJsonReadersLow with UnitJsonReaders {
	implicit def EitherReader[T1:JsonReader,T2:JsonReader]:JsonReader[Either[T1,T2]]	=
			ASC altReader ("left", "right")

	/*
	// replaced by OptionOptionReader and OptionReader in JsonReadersLow
	implicit def OptionReader[T:JsonReader]:JsonReader[Option[T]]	=
			altReader[Unit,T]("none", "some")	>=>
			eitherToOption
	*/

	implicit def OptionOptionReader[T](implicit ev:JsonReader[Option[T]]):JsonReader[Option[Option[T]]]	=
			ASC.altReader[Unit,Option[T]]("none", "some")	>=>
			eitherToOption

	/*
	// in old times, OptionReader would have broken for Option[Unit] because Unit was encoded as JsonNull just like None.
	implicit def OptionUnitReader:JsonReader[Option[Unit]]	=
			JC.expectBoolean	map {
				case true	=> Some(())
				case false	=> None
			}
	*/

	//------------------------------------------------------------------------------

	private def eitherToOption[E,T]:Converter[E,Either[Unit,T],Option[T]]	=
			Converter total (_.right.toOption)
}

trait SumJsonReadersLow {
	implicit def OptionReader[T:JsonReader]:JsonReader[Option[T]]	=
			Converter {
				case JsonNull	=> Validated good None
				case x			=> implicitly[JsonReader[T]] apply x map Some.apply
			}
}
