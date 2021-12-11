package scjson.converter

import scjson.converter.{
	JsonConverters	as JC,
	KeyConverters	as KC
}

/** JsonKeyReader can double JsonReader */
trait KeyJsonReaders {
	// NOTE you need to call this explicitly
	def keyToJsonReader[T](using ev:JsonKeyReader[T]):JsonReader[T]	=
		JC.expectString >=> KC.StringToKey >=> ev
}
