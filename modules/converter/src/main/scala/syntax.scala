package scjson.converter

import scutil.base.implicits._

import scjson.ast._

object syntax {
	def jsonValue(value:JsonWrapper):JsonResult[JsonValue]	=
		value.unwrap

	def jsonArray(values:JsonWrapper*):JsonResult[JsonValue]	=
		values.toVector traverseValidated (_.unwrap) map JsonArray.apply

	def jsonObject(values:(String,JsonWrapper)*):JsonResult[JsonValue]	=
		values.toVector traverseValidated { case (key, value) => value.unwrap map (key -> _) } map JsonObject.apply

	//------------------------------------------------------------------------------

	object JsonWrapper {
		implicit def JsonWrapperUsingJsonWriter[T:JsonWriter](it:T):JsonWrapper	=
			JsonWrapper(JsonWriter[T] convert it)

		implicit def JsonWrapperFromJsonResult(it:JsonResult[JsonValue]):JsonWrapper	=
			JsonWrapper(it)
	}

	final case class JsonWrapper(unwrap:JsonResult[JsonValue])
}
