package scjson.converter

import minitest._

import scjson.ast._

object NullOptionTest extends SimpleTestSuite {
	import JsonFormat._

	//------------------------------------------------------------------------------

	test("plain option should serialize None as null") {
		assertEquals(
			writeAst(None:Option[String]),
			Right(JsonNull)
		)
	}
	test("plain option should serialize Some as value") {
		assertEquals(
			writeAst(Some("hallo"):Option[String]),
			Right(JsonString("hallo"))
		)
	}

	//------------------------------------------------------------------------------

	test("nested option should serialize None as {none:{}}") {
		assertEquals(
			writeAst(None:Option[Option[String]]),
			Right(JsonObject.Var("none" -> Empty))
		)
	}
	test("nested option should serialize Some(None) as {some:null}") {
		assertEquals(
			writeAst(Some(None):Option[Option[String]]),
			Right(JsonObject.Var("some" -> JsonNull))
		)
	}
	test("nested option should serialize Some(Some) as {some:value}") {
		assertEquals(
			writeAst(Some(Some("hallo")):Option[Option[String]]),
			Right(JsonObject.Var("some" -> JsonString("hallo")))
		)
	}

	//------------------------------------------------------------------------------

	test("double nested option should serialize None as {none:{}}") {
		assertEquals(
			writeAst(None:Option[Option[Option[String]]]),
			Right(JsonObject.Var("none" -> Empty))
		)
	}
	test("double nested option should serialize Some(None) as {some:{none:{}}}") {
		assertEquals(
			writeAst(Some(None):Option[Option[Option[String]]]),
			Right(JsonObject.Var("some" -> JsonObject.Var("none" -> Empty)))
		)
	}
	test("double nested option should serialize Some(Some(None)) as {some:{some:null}}") {
		assertEquals(
			writeAst(Some(Some(None)):Option[Option[Option[String]]]),
			Right(JsonObject.Var("some" -> JsonObject.Var("some" -> JsonNull)))
		)
	}
	test("double nested option should serialize Some(Some(Some)) as {some:{some:value}}") {
		assertEquals(
			writeAst(Some(Some(Some("hallo"))):Option[Option[Option[String]]]),
			Right(JsonObject.Var("some" -> JsonObject.Var("some" -> JsonString("hallo"))))
		)
	}

	//------------------------------------------------------------------------------

	// TODO it seems in old times, we encoded ```none``` as ```{ "none": true }``` and now we use ```{ "none": {} }```
	//	JsonTrue
	def Empty:JsonValue	= JsonObject.Var()

	def readAst[T:JsonReader](json:JsonValue):Either[JsonError,T]	=
		JsonReader[T].convert(json).toEither

	def writeAst[T:JsonWriter](value:T):Either[JsonError,JsonValue]	=
		JsonWriter[T].convert(value).toEither
}
