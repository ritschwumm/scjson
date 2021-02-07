package scjson.pickle.protocol

import scjson.ast._
import scjson.pickle._

import JsonPickleUtil._

object SeqProtocol extends SeqProtocol

trait SeqProtocol {
	implicit def SeqFormat[T:Format]:Format[Seq[T]] =
		Format[Seq[T]](
			(out:Seq[T])	=> JsonValue.fromItems(out map doWrite[T]),
			(in:JsonValue)	=> arrayValue(in) map doReadUnsafe[T]
		)
}
