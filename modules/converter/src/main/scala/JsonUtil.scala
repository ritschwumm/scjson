package scjson.converter

import scutil.lang.*
import scutil.core.implicits.*
import scjson.ast.*
import scjson.codec.*

object JsonUtil {
	def stringToValue[T:JsonReader]:JsonConverter[String,T]	=
		stringToJson >=> JsonReader[T]

	def valueToString[T:JsonWriter]:JsonConverter[T,String]	=
		JsonWriter[T] >=> jsonToString

	//------------------------------------------------------------------------------

	val jsonToString:JsonConverter[JsonValue,String]	=
		Converter.total(JsonCodec.encodeShort)

	val stringToJson:JsonConverter[String,JsonValue]	=
		Converter { it =>
			// TODO swallows the "looking at" information. sad...
			JsonCodec.decode(it).leftMap { it => Nes.one(it.message) }.toValidated
		}
}
