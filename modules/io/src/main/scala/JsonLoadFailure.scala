package scjson.io

import java.io.*

import scjson.codec.*
import scjson.converter.*

object JsonLoadFailure {
	final case class IoException(cause:IOException)			extends JsonLoadFailure
	final case class DecodeFailure(base:JsonDecodeFailure)	extends JsonLoadFailure
	final case class ParseFailure(base:JsonError)			extends JsonLoadFailure
}

sealed trait JsonLoadFailure
