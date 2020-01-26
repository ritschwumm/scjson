package scjson.io.pickle

import java.io._

object JsonSaveFailure {
	final case class IoException(cause:IOException)	extends JsonSaveFailure
}

sealed trait JsonSaveFailure
