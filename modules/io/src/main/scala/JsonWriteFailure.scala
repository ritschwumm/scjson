package scjson.io

import scjson.converter.*

object JsonWriteFailure {
	final case class UnparseFailure(base:JsonError)	extends JsonWriteFailure
}

sealed trait JsonWriteFailure
