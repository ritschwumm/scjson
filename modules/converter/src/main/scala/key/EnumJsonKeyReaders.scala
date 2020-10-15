package scjson.converter

import scutil.lang.implicits._
import scutil.lang._

import scjson.converter.{
	KeyConverters	=> KC
}

trait EnumJsonKeyReaders {
	def enumKeyReader[T](func:String=>Option[T]):JsonKeyReader[T]	=
		KC.KeyToString	>=>
		Converter.optional(func, JsonError(show"unexpected enum value"))

	def enumKeyReaderPf[T](func:PartialFunction[String,T]):JsonKeyReader[T]	=
		enumKeyReader(func.lift)
}
