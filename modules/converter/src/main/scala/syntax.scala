package scjson.converter

import scala.language.implicitConversions

import scutil.core.implicits.*

import scjson.ast.*

object syntax {
	def jsonValue(value:JsonWrapper):JsonResult[JsonValue]	=
		value.unwrap

	def jsonArray(values:JsonWrapper*):JsonResult[JsonValue]	=
		values.toVector.traverseValidated(_.unwrap).map(JsonValue.fromItems)

	def jsonObject(values:(String,JsonWrapper)*):JsonResult[JsonValue]	=
		values.toVector.traverseValidated{ (key, value) => value.unwrap.map(key -> _) }.map(JsonValue.fromFields)

	//------------------------------------------------------------------------------

	object JsonWrapper {
		implicit def JsonWrapperUsingJsonWriter[T:JsonWriter](it:T):JsonWrapper	=
			JsonWrapper(JsonWriter[T].convert(it))

		implicit def JsonWrapperFromJsonResult(it:JsonResult[JsonValue]):JsonWrapper	=
			JsonWrapper(it)
	}

	final case class JsonWrapper(unwrap:JsonResult[JsonValue])
}
