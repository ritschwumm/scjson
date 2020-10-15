package scjson.converter

import scjson.converter.{
	AltSeqConverters	=> ASC
}

trait EitherJsonWriters {
	implicit def EitherWriter[T1:JsonWriter,T2:JsonWriter]:JsonWriter[Either[T1,T2]]	=
		ASC.altWriter("left", "right")
}
