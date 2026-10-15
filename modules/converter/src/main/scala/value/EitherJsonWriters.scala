package scjson.converter

trait EitherJsonWriters {
	given EitherWriter: [T1:JsonWriter,T2:JsonWriter] => JsonWriter[Either[T1,T2]]	=
		AltSeqConverters.altWriter("left", "right")
}
