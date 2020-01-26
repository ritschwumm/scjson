package scjson.converter

import org.specs2.mutable._

import scutil.lang._

import scjson.ast._
import scjson.converter.syntax._
import JsonWriters._

class SyntaxTest extends Specification {
	"syntax should" should {
		"work with simple values" in {
			jsonValue(1) mustEqual Good(JsonNumber(1))
		}
		"work with arrays" in {
			jsonArray(1, "test", false) mustEqual
			Good(
				JsonArray.Var(
					JsonNumber(1),
					JsonString("test"),
					JsonFalse
				)
			)
		}
		"work with objects" in {
			// TODO implicit lookup failing for JsonNull (as a subtype of JsonValue) sucks
			jsonObject("a" -> 1, "b" -> "x", "c" -> (JsonNull:JsonValue))	mustEqual
			Good(
				JsonObject.Var(
					"a"	-> JsonNumber(1),
					"b"	-> JsonString("x"),
					"c"	-> JsonNull
				)
			)
		}
		"work with nested objects" in {
			jsonObject(
				"a" -> 1,
				"b" -> jsonObject(
					"x"	-> 2
				)
			)	mustEqual
			Good(
				JsonObject.Var(
					"a"	-> JsonNumber(1),
					"b"	-> JsonObject.Var(
						"x"	-> JsonNumber(2)
					)
				)
			)
		}
	}
}
