package scjson.pickle.protocol

import scutil.lang.ISeq

import scjson.ast._
import scjson.pickle._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumFormat[T](values:ISeq[(String,T)]):Format[T]	=
			Format[T](
				(values map { case (k,v) => (v, JSONString(k)) }).toMap,
				(values map { case (k,v) => (JSONString(k), v) }).toMap
			)
}
