package scjson.converter

/** JsonKeyWriter can double JsonWriter */
trait KeyJsonWriters {
	// NOTE you need to call this explicitly
	def keyToJsonWriter[T](using ev:JsonKeyWriter[T]):JsonWriter[T]	=
		ev >=> KeyConverters.KeyToString >=> JsonConverters.makeString
}
