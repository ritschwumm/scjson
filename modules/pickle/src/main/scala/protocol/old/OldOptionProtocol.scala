package scjson.pickle.protocol.old

import scjson.pickle._

import scjson.pickle.protocol.OptionProtocols

object OldOptionProtocol extends OldOptionProtocol

trait OldOptionProtocol {
	// alternative {some} or {none}
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
		OptionProtocols.adtFormat
}
