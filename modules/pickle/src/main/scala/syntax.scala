package scjson.pickle

import scjson.ast._

package object syntax {
	def jsonValue(value:JsonWrapper):JsonValue	=
		value.unwrap

	def jsonArray(values:JsonWrapper*):JsonArray	=
		JsonArray(values.toVector map { _.unwrap })

	def jsonObject(values:(String,JsonWrapper)*):JsonObject	=
		JsonObject(values.toVector map { case (k, v) => (k, v.unwrap) })
}
