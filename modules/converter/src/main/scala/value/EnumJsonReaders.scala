package scjson.converter

import scutil.lang.implicits._
import scutil.lang._

import scjson.converter.{
	JsonConverters	=> JC
}

trait EnumJsonReaders {
	def enumReader[T](func:String=>Option[T]):JsonReader[T]	=
		JC.expectString >=>
		(Converter optional (func, JsonError(show"unexpected enum value")))

	def enumReaderPf[T](func:PartialFunction[String,T]):JsonReader[T]	=
		enumReader(func.lift)
}
