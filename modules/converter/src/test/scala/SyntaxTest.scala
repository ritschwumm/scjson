package scjson.converter

import minitest._

import scutil.lang._

import scjson.ast._
import scjson.converter.syntax._
import JsonWriters._

object SyntaxTest extends SimpleTestSuite {
	test("syntax should work with simple values") {
		assertEquals(
			jsonValue(1),
			Good(JsonNumber(1))
		)
	}
	test("syntax should work with arrays") {
		assertEquals(
			jsonArray(1, "test", false),
			Good(
				JsonArray.Var(
					JsonNumber(1),
					JsonString("test"),
					JsonFalse
				)
			)
		)
	}
	test("syntax should work with objects") {
		// TODO implicit lookup failing for JsonNull (as a subtype of JsonValue) sucks
		assertEquals(
			jsonObject("a" -> 1, "b" -> "x", "c" -> (JsonNull:JsonValue)),
			Good(
				JsonObject.Var(
					"a"	-> JsonNumber(1),
					"b"	-> JsonString("x"),
					"c"	-> JsonNull
				)
			)
		)
	}
	test("syntax should work with nested objects") {
		assertEquals(
			jsonObject(
				"a" -> 1,
				"b" -> jsonObject(
					"x"	-> 2
				)
			),
			Good(
				JsonObject.Var(
					"a"	-> JsonNumber(1),
					"b"	-> JsonObject.Var(
						"x"	-> JsonNumber(2)
					)
				)
			)
		)
	}
}
