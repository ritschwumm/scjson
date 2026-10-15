package scjson.converter

import scutil.lang.implicits.*
import scutil.lang.*

trait EnumJsonReaders {
	def enumReader[T](func:String=>Option[T]):JsonReader[T]	=
		JsonConverters.expectString >=>
		Converter.optional(func, JsonError(show"unexpected enum value"))

	def enumReaderPf[T](func:PartialFunction[String, T]):JsonReader[T]	=
		enumReader(func.lift)
}
