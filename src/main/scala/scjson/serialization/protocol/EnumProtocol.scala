package scjson.serialization

import scjson._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumJSONFormat[T](values:Seq[(String,T)]):JSONFormat[T]	=
			JSONFormat[T](
					values map { case (k,v) => (v, JSONString(k)) } toMap,
					values map { case (k,v) => (JSONString(k), v) } toMap)
}
