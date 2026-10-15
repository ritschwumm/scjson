package scjson.converter

import scala.Conversion.into

import scutil.core.implicits.*

import scjson.ast.*

object syntax {
	def jsonValue(value:into[JsonValueWrapper]):JsonResult[JsonValue]	=
		value.unwrap

	def jsonArray(values:into[JsonValueWrapper]*):JsonResult[JsonValue]	=
		values.toVector.traverseValidated(_.unwrap).map(JsonValue.fromItems)

	def jsonObject(values:into[JsonFieldWrapper]*):JsonResult[JsonValue]	=
		values.toVector.traverseValidated{ field => field.unwrap.map(field.key -> _) }.map(JsonValue.fromFields)

	//------------------------------------------------------------------------------

	object JsonValueWrapper {
		given [T:JsonWriter] => Conversion[T, JsonValueWrapper]	=
			value => JsonValueWrapper(JsonWriter[T].convert(value))

		given Conversion[JsonResult[JsonValue], JsonValueWrapper]	=
			value => JsonValueWrapper(value)
	}

	final case class JsonValueWrapper(unwrap:JsonResult[JsonValue])

	//-----------------------------------------------------------------------------

	// into[(String, JsonValueWrapper)]* does not work,
	// (String, into[JsonValueWrapper])* does not work either
	object JsonFieldWrapper {
		given [T:JsonWriter] => Conversion[(String, T), JsonFieldWrapper]	=
			(key, value) => JsonFieldWrapper(key, JsonWriter[T].convert(value))

		given Conversion[(String, JsonResult[JsonValue]), JsonFieldWrapper]	=
			(key, value) => JsonFieldWrapper(key, value)
	}

	final case class JsonFieldWrapper(key:String, unwrap:JsonResult[JsonValue])

}
