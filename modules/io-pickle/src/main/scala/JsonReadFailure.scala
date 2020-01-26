package scjson.io.pickle

import scjson.codec._
import scjson.pickle._

object JsonReadFailure {
	final case class DecodeFailure(base:JsonDecodeFailure)		extends JsonReadFailure
	final case class UnpickleFailure(base:JsonUnpickleFailure)	extends JsonReadFailure
}

sealed trait JsonReadFailure
