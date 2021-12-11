package scjson.converter

import scutil.lang.implicits.*
import scutil.lang.*

import scjson.converter.{
	KeyConverters	as KC
}

trait EnumJsonKeyReaders {
	def enumKeyReader[T](func:String=>Option[T]):JsonKeyReader[T]	=
		KC.KeyToString	>=>
		Converter.optional(func, JsonError(show"unexpected enum value"))

	def enumKeyReaderPf[T](func:PartialFunction[String,T]):JsonKeyReader[T]	=
		enumKeyReader(func.lift)
}
