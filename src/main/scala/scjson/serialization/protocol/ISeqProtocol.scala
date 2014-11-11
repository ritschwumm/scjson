package scjson.serialization

import scutil.lang._

import scjson._

import JSONSerializationUtil._

object ISeqProtocol extends ISeqProtocol

trait ISeqProtocol {
	implicit def ISeqFormat[T:Format]:Format[ISeq[T]] = 
			Format[ISeq[T]](
				(out:ISeq[T])	=> JSONArray(out map doWrite[T]),
				(in:JSONValue)	=> arrayValue(in) map doRead[T]
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
