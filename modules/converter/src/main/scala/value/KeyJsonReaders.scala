package scjson.converter

import scjson.converter.{
	JsonConverters	=> JC,
	KeyConverters	=> KC
}

/** JsonKeyReader can double JsonReader */
trait KeyJsonReaders {
	// NOTE you need to call this explicitly
	def keyToJsonReader[T](implicit ev:JsonKeyReader[T]):JsonReader[T]	=
		JC.expectString >=> KC.StringToKey >=> ev
}
