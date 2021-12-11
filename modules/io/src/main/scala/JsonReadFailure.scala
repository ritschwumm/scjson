package scjson.io

import scjson.codec.*
import scjson.converter.*

object JsonReadFailure {
	final case class DecodeFailure(base:JsonDecodeFailure)	extends JsonReadFailure
	final case class ParseFailure(base:JsonError)			extends JsonReadFailure
}

sealed trait JsonReadFailure
