package scjson.pickle.protocol

import scjson.pickle._

object NullOptionProtocol extends NullOptionProtocol

trait NullOptionProtocol extends LowPrioNullOptionProtocol {
	// alternative {some} or {none}
	implicit def OptionOptionFormat[T](implicit ev:Format[Option[T]]):Format[Option[Option[T]]]	=
		OptionProtocols.adtFormat
}

trait LowPrioNullOptionProtocol {
	// alternative value or null
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
		OptionProtocols.nullFormat
}
