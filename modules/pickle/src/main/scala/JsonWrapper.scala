package scjson.pickle.syntax

import scjson.ast._
import scjson.pickle._

object JsonWrapper {
	implicit def toJsonWrapper[T:Format](it:T):JsonWrapper	=
		new JsonWrapper(format[T] get it)
}

final case class JsonWrapper(unwrap:JsonValue)
