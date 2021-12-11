package scjson.io

import java.io.*

import scjson.converter.*

object JsonSaveFailure {
	final case class IoException(cause:IOException)	extends JsonSaveFailure
	final case class UnparseFailure(base:JsonError)	extends JsonSaveFailure
}

sealed trait JsonSaveFailure
