package scjson.io

import java.io._

import scjson.codec._
import scjson.converter._

object JsonLoadFailure {
	final case class IoException(cause:IOException)			extends JsonLoadFailure
	final case class DecodeFailure(base:JsonDecodeFailure)	extends JsonLoadFailure
	final case class ParseFailure(base:JsonError)			extends JsonLoadFailure
}

sealed trait JsonLoadFailure
