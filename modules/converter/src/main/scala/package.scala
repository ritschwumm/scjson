package scjson

import scutil.lang._
import scjson.ast._

package object converter {
	// BETTER use a sum type instead of a simple String
	type JsonError	= Nes[String]
	def JsonError(s:String):JsonError	= Nes one s
	def JsonInvalid[T](s:String):JsonResult[T]	= Validated.invalid(JsonError(s))

	type JsonResult[T]		= Validated[JsonError,T]
	type JsonConverter[S,T]	= Converter[JsonError,S,T]

	//------------------------------------------------------------------------------

	type JsonReader[T]	= JsonConverter[JsonValue,T]
	type JsonWriter[T]	= JsonConverter[T,JsonValue]

	def JsonReader[T:JsonReader]:JsonReader[T]	= implicitly[JsonReader[T]]
	def JsonWriter[T:JsonWriter]:JsonWriter[T]	= implicitly[JsonWriter[T]]

	//------------------------------------------------------------------------------

	type JsonKeyReader[T]	= JsonConverter[JsonKey,T]
	type JsonKeyWriter[T]	= JsonConverter[T,JsonKey]

	def JsonKeyReader[T:JsonKeyReader]:JsonKeyReader[T]	= implicitly[JsonKeyReader[T]]
	def JsonKeyWriter[T:JsonKeyWriter]:JsonKeyWriter[T]	= implicitly[JsonKeyWriter[T]]
}
