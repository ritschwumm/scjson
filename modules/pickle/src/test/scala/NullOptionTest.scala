package scjson.pickle

import minitest._

import scjson.ast._
import scjson.pickle.protocol._

object NullOptionTest extends SimpleTestSuite {
	import StandardProtocol._

	//------------------------------------------------------------------------------

	test("plain option should serialize None as null") {
		assertEquals(
			doWrite(None:Option[String]),
			JsonNull
		)
	}
	test("plain option should serialize Some as value") {
		assertEquals(
			doWrite(Some("hallo"):Option[String]),
			JsonString("hallo")
		)
	}

	//------------------------------------------------------------------------------

	test("nested option should serialize None as {none:true}") {
		assertEquals(
			doWrite(None:Option[Option[String]]),
			JsonObject.Var("none" -> Empty)
		)
	}
	test("nested option should serialize Some(None) as {some:null}") {
		assertEquals(
			doWrite(Some(None):Option[Option[String]]),
			JsonObject.Var("some" -> JsonNull)
		)
	}
	test("nested option should serialize Some(Some) as {some:value}") {
		assertEquals(
			doWrite(Some(Some("hallo")):Option[Option[String]]),
			JsonObject.Var("some" -> JsonString("hallo"))
		)
	}

	//------------------------------------------------------------------------------

	test("double nested option should serialize None as {none:true}") {
		assertEquals(
			doWrite(None:Option[Option[Option[String]]]),
			JsonObject.Var("none" -> Empty)
		)
	}
	test("double nested option should serialize Some(None) as {some:{none:true}}") {
		assertEquals(
			doWrite(Some(None):Option[Option[Option[String]]]),
			JsonObject.Var("some" -> JsonObject.Var("none" -> Empty))
		)
	}
	test("double nested option should serialize Some(Some(None)) as {some:{some:null}}") {
		assertEquals(
			doWrite(Some(Some(None)):Option[Option[Option[String]]]),
			JsonObject.Var("some" -> JsonObject.Var("some" -> JsonNull))
		)
	}
	test("double nested option should serialize Some(Some(Some)) as {some:{some:value}}") {
		assertEquals(
			doWrite(Some(Some(Some("hallo"))):Option[Option[Option[String]]]),
			JsonObject.Var("some" -> JsonObject.Var("some" -> JsonString("hallo")))
		)
	}

	//------------------------------------------------------------------------------

	// TODO it seems in old times, we encoded ```none``` as ```{ "none": true }``` and now we use ```{ "none": {} }```
	def Empty:JsonValue	= JsonTrue
}
