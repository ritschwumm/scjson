package scjson.converter

import scutil.lang._

import scjson.converter.{
	KeyConverters	=> KC
}

trait EnumJsonKeyWriters {
	def enumKeyWriter[T](func:T=>String):JsonKeyWriter[T]	=
			(Converter total func) >=>
			KC.StringToKey
}
