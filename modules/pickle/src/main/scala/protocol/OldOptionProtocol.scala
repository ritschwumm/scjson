package scjson.pickle.protocol

import scjson.pickle._

object OldOptionProtocol extends OldOptionProtocol

trait OldOptionProtocol {
	// alternative {some} or {none}
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
		OptionProtocols.adtFormat
}
