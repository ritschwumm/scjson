package scjson.converter

import scutil.lang.*

import scjson.converter.{
	KeyConverters	as KC
}

trait EnumJsonKeyWriters {
	def enumKeyWriter[T](func:T=>String):JsonKeyWriter[T]	=
		(Converter total func) >=>
		KC.StringToKey
}
