package scjson.converter

/** JsonKeyReader can double JsonReader */
trait KeyJsonReaders {
	// NOTE you need to call this explicitly
	def keyToJsonReader[T](using ev:JsonKeyReader[T]):JsonReader[T]	=
		JsonConverters.expectString >=> KeyConverters.StringToKey >=> ev
}
