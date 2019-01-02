package scjson.pickle

import scjson.ast._

package object syntax {
	// NOTE must not be passed null values

	def jsonArray(values:JsonWrapper*):JsonArray	=
			JsonArray(values.toVector map { _.unwrap })

	def jsonObject(values:(String,JsonWrapper)*):JsonObject	=
			JsonObject(values.toVector map { case (k, v) => (k, v.unwrap) })

	def jsonSimple(value:JsonWrapper):JsonValue	=
			value.unwrap

	//------------------------------------------------------------------------------

	implicit def toJsonWrapper[T:Format](it:T):JsonWrapper	=
			new JsonWrapper(format[T] get it)
}
