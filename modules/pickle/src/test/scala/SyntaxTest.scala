package scjson.pickle.syntax

import minitest._

import scjson.ast._
import scjson.pickle.protocol.StandardProtocol._

object SyntaxTest extends SimpleTestSuite {
	test("syntax should work with simple values") {
		assertEquals(
			jsonValue(1),
			JsonNumber(1)
		)
	}
	test("syntax should work with arrays") {
		assertEquals(
			jsonArray(1, "test", false),
			JsonArray.Var(
				JsonNumber(1),
				JsonString("test"),
				JsonFalse
			)
		)
	}
	test("syntax should work with objects") {
		assertEquals(
			jsonObject("a" -> 1, "b" -> "x", "c" -> JsonNull),
			JsonObject.Var(
				"a"	-> JsonNumber(1),
				"b"	-> JsonString("x"),
				"c"	-> JsonNull
			)
		)
	}
}
