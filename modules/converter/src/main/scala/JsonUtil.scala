package scjson.converter

import scutil.lang._
import scutil.base.implicits._
import scjson.ast._
import scjson.codec._

object JsonUtil {
	def stringToValue[T:JsonReader]:JsonConverter[String,T]	=
		stringToJson >=> JsonReader[T]

	def valueToString[T:JsonWriter]:JsonConverter[T,String]	=
		JsonWriter[T] >=> jsonToString

	//------------------------------------------------------------------------------

	val jsonToString:JsonConverter[JsonValue,String]	=
		Converter total JsonCodec.encodeShort

	val stringToJson:JsonConverter[String,JsonValue]	=
		Converter { it =>
			(JsonCodec decode it leftMap { it => Nes single it.message }).toValidated
		}
}
