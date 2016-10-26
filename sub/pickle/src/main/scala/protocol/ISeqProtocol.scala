package scjson.pickle.protocol

import scutil.lang._

import scjson.ast._
import scjson.pickle._

import JSONPickleUtil._

object ISeqProtocol extends ISeqProtocol

trait ISeqProtocol {
	implicit def ISeqFormat[T:Format]:Format[ISeq[T]] =
			Format[ISeq[T]](
				(out:ISeq[T])	=> JSONArray(out map doWrite[T]),
				(in:JSONValue)	=> arrayValue(in) map doReadUnsafe[T]
			)
			
	/*
	implicit def ISeqFormat[T:Format]:Format[ISeq[T]]	= {
		val sub	= format[T]
		SubtypeFormat[ISeq[T],JSONArray](
			it	=> JSONArray(it map doWrite[T]),
			it	=> it.value map sub.read
		)
	}
	*/
}
