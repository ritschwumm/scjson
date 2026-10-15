package scjson.converter

import scutil.lang.*

trait EnumJsonWriters {
	def enumWriter[T](func:T=>String):JsonWriter[T]	=
		Converter.total(func) >=>
		JsonConverters.makeString
}
