package scjson.io.converter

import scjson.converter._

object JsonWriteFailure {
	final case class UnparseFailure(base:JsonError)	extends JsonWriteFailure
}

sealed trait JsonWriteFailure
