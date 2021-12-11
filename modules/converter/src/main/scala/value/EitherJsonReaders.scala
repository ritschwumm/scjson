package scjson.converter

import scjson.converter.{
	AltSeqConverters	as ASC
}

trait EitherJsonReaders {
	given EitherReader[T1:JsonReader,T2:JsonReader]:JsonReader[Either[T1,T2]]	=
		ASC.altReader("left", "right")
}
