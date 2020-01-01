package scjson.converter

import scjson.converter.{
	JsonConverters	=> JC,
	KeyConverters	=> KC
}

object KeyJsonWriters extends KeyJsonWriters

trait KeyJsonWriters {
	// NOTE you need to call this explicitly
	def keyToJsonWriter[T](implicit ev:JsonKeyWriter[T]):JsonWriter[T]	=
		ev >=> KC.KeyToString >=> JC.makeString
}
