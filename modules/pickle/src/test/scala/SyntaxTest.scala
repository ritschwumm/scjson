package scjson.pickle.syntax

import minitest._

import scjson.ast._
import scjson.pickle.protocol.StandardProtocol._

object SyntaxTest extends SimpleTestSuite {
	test("syntax should work with simple values") {
		assertEquals(
			jsonValue(1),
			JsonValue.fromInt(1)
		)
	}
	test("syntax should work with arrays") {
		assertEquals(
			jsonArray(1, "test", false),
			JsonValue.arr(
				JsonValue.fromInt(1),
				JsonValue.fromString("test"),
				JsonValue.False
			)
		)
	}
	test("syntax should work with objects") {
		assertEquals(
			jsonObject("a" -> 1, "b" -> "x", "c" -> JsonValue.Null),
			JsonValue.obj(
				"a"	-> JsonValue.fromInt(1),
				"b"	-> JsonValue.fromString("x"),
				"c"	-> JsonValue.Null
			)
		)
	}
}
