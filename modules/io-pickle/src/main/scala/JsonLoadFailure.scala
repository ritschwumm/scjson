package scjson.io.pickle

import java.io._

import scjson.codec._
import scjson.pickle._

object JsonLoadFailure {
	final case class IoException(cause:IOException)				extends JsonLoadFailure
	final case class DecodeFailure(base:JsonDecodeFailure)		extends JsonLoadFailure
	final case class UnpickleFailure(base:JsonUnpickleFailure)	extends JsonLoadFailure
}

sealed trait JsonLoadFailure
