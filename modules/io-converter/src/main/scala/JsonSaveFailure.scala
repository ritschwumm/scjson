package scjson.io.converter

import java.io._

import scjson.converter._

object JsonSaveFailure {
	final case class IoException(cause:IOException)	extends JsonSaveFailure
	final case class UnparseFailure(base:JsonError)	extends JsonSaveFailure
}

sealed trait JsonSaveFailure
