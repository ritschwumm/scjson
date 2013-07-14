package scjson.serialization

import scjson._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumFormat[T](values:Seq[(String,T)]):Format[T]	=
			Format[T](
					values map { case (k,v) => (v, JSONString(k)) } toMap,
					values map { case (k,v) => (JSONString(k), v) } toMap)
}
