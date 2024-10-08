package scjson.converter

import scutil.lang.implicits.*
import scutil.lang.*

import scjson.converter.{
	JsonConverters	as JC
}

trait EnumJsonReaders {
	def enumReader[T](func:String=>Option[T]):JsonReader[T]	=
		JC.expectString >=>
		Converter.optional(func, JsonError(show"unexpected enum value"))

	def enumReaderPf[T](func:PartialFunction[String,T]):JsonReader[T]	=
		enumReader(func.lift)
}
