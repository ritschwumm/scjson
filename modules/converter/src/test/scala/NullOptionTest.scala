package scjson.converter

import minitest.*

import scjson.ast.*

object NullOptionTest extends SimpleTestSuite {
	import JsonFormat.given

	//------------------------------------------------------------------------------

	test("plain option should serialize None as null") {
		assertEquals(
			writeAst(None:Option[String]),
			Right(JsonValue.Null)
		)
	}
	test("plain option should serialize Some as value") {
		assertEquals(
			writeAst(Some("hallo"):Option[String]),
			Right(JsonValue.fromString("hallo"))
		)
	}

	//------------------------------------------------------------------------------

	test("nested option should serialize None as {none:{}}") {
		assertEquals(
			writeAst(None:Option[Option[String]]),
			Right(JsonValue.obj("none" -> Empty))
		)
	}
	test("nested option should serialize Some(None) as {some:null}") {
		assertEquals(
			writeAst(Some(None):Option[Option[String]]),
			Right(JsonValue.obj("some" -> JsonValue.Null))
		)
	}
	test("nested option should serialize Some(Some) as {some:value}") {
		assertEquals(
			writeAst(Some(Some("hallo")):Option[Option[String]]),
			Right(JsonValue.obj("some" -> JsonValue.fromString("hallo")))
		)
	}

	//------------------------------------------------------------------------------

	test("double nested option should serialize None as {none:{}}") {
		assertEquals(
			writeAst(None:Option[Option[Option[String]]]),
			Right(JsonValue.obj("none" -> Empty))
		)
	}
	test("double nested option should serialize Some(None) as {some:{none:{}}}") {
		assertEquals(
			writeAst(Some(None):Option[Option[Option[String]]]),
			Right(JsonValue.obj("some" -> JsonValue.obj("none" -> Empty)))
		)
	}
	test("double nested option should serialize Some(Some(None)) as {some:{some:null}}") {
		assertEquals(
			writeAst(Some(Some(None)):Option[Option[Option[String]]]),
			Right(JsonValue.obj("some" -> JsonValue.obj("some" -> JsonValue.Null)))
		)
	}
	test("double nested option should serialize Some(Some(Some)) as {some:{some:value}}") {
		assertEquals(
			writeAst(Some(Some(Some("hallo"))):Option[Option[Option[String]]]),
			Right(JsonValue.obj("some" -> JsonValue.obj("some" -> JsonValue.fromString("hallo"))))
		)
	}

	//------------------------------------------------------------------------------

	// TODO it seems in old times, we encoded ```none``` as ```{ "none": true }``` and now we use ```{ "none": {} }```
	def Empty:JsonValue	= JsonValue.emptyObject

	def readAst[T:JsonReader](json:JsonValue):Either[JsonError,T]	=
		JsonReader[T].convert(json).toEither

	def writeAst[T:JsonWriter](value:T):Either[JsonError,JsonValue]	=
		JsonWriter[T].convert(value).toEither
}
