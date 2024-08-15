package scjson.converter

import scutil.lang.*

import scjson.converter.{
	JsonConverters	as JC
}

trait EnumJsonWriters {
	def enumWriter[T](func:T=>String):JsonWriter[T]	=
		Converter.total(func) >=>
		JC.makeString
}
