package scjson.converter

import scutil.lang._

import scjson.converter.{
	JsonConverters	=> JC
}

trait EnumJsonWriters {
	def enumWriter[T](func:T=>String):JsonWriter[T]	=
		(Converter total func) >=>
		JC.makeString
}
