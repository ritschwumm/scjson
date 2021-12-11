package scjson.converter

import scjson.converter.{
	JsonConverters	as JC,
	KeyConverters	as KC
}

/** JsonKeyWriter can double JsonWriter */
trait KeyJsonWriters {
	// NOTE you need to call this explicitly
	def keyToJsonWriter[T](using ev:JsonKeyWriter[T]):JsonWriter[T]	=
		ev >=> KC.KeyToString >=> JC.makeString
}
