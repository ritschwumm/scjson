package scjson.pickle

import scjson.ast._

package object syntax {
	def jsonValue(value:JsonWrapper):JsonValue	=
		value.unwrap

	def jsonArray(values:JsonWrapper*):JsonValue	=
		JsonValue.fromItems(values.toVector map { _.unwrap })

	def jsonObject(values:(String,JsonWrapper)*):JsonValue	=
		JsonValue.fromFields(values.toVector map { case (k, v) => (k, v.unwrap) })
}
