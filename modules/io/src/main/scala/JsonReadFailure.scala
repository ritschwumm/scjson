package scjson.io

import scjson.codec._
import scjson.converter._

object JsonReadFailure {
	final case class DecodeFailure(base:JsonDecodeFailure)	extends JsonReadFailure
	final case class ParseFailure(base:JsonError)			extends JsonReadFailure
}

sealed trait JsonReadFailure
