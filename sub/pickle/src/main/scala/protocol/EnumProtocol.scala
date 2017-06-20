package scjson.pickle.protocol

import scutil.base.implicits._
import scutil.lang.ISeq

import scjson.ast._
import scjson.pickle._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumFormat[T](values:ISeq[(String,T)]):Format[T]	=
			Format[T](
				values mapToMap { case (k,v) => (v, JSONString(k)) },
				values mapToMap { case (k,v) => (JSONString(k), v) }
			)
}
