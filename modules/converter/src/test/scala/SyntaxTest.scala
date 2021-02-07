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
			Validated.valid(JsonValue.fromInt(1))
		)
	}
	test("syntax should work with arrays") {
		assertEquals(
			jsonArray(1, "test", false),
			Validated.valid(
				JsonValue.arr(
					JsonValue.fromInt(1),
					JsonValue.fromString("test"),
					JsonValue.False
				)
			)
		)
	}
	test("syntax should work with objects") {
		assertEquals(
			jsonObject("a" -> 1, "b" -> "x", "c" -> JsonValue.Null),
			Validated.valid(
				JsonValue.obj(
					"a"	-> JsonValue.fromInt(1),
					"b"	-> JsonValue.fromString("x"),
					"c"	-> JsonValue.Null
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
			Validated.valid(
				JsonValue.obj(
					"a"	-> JsonValue.fromInt(1),
					"b"	-> JsonValue.obj(
						"x"	-> JsonValue.fromInt(2)
					)
				)
			)
		)
	}
}
