package scjson.pickle.protocol

import scutil.core.implicits._

import scjson.ast._
import scjson.pickle._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumFormat[T](values:Seq[(String,T)]):Format[T]	=
		Format[T](
			values mapToMap { case (k,v) => (v, JsonString(k)) },
			values mapToMap { case (k,v) => (JsonString(k), v) }
		)
}
