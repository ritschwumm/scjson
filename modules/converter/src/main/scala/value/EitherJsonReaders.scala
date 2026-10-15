package scjson.converter

trait EitherJsonReaders {
	given EitherReader: [T1:JsonReader,T2:JsonReader] => JsonReader[Either[T1,T2]]	=
		AltSeqConverters.altReader("left", "right")
}
