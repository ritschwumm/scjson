package scjson.converter

import scutil.lang.*

trait EnumJsonKeyWriters {
	def enumKeyWriter[T](func:T=>String):JsonKeyWriter[T]	=
		Converter.total(func) >=>
		KeyConverters.StringToKey
}
