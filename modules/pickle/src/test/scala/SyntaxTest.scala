package scjson.pickle.syntax

import org.specs2.mutable._

import scjson.ast._
import scjson.pickle.protocol.StandardProtocol._

class SyntaxTest extends Specification {
	"syntax should" should {
		"work with simple values" in {
			jsonValue(1) mustEqual JsonNumber(1)
		}
		"work with arrays" in {
			jsonArray(1, "test", false) mustEqual
			JsonArray.Var(
				JsonNumber(1),
				JsonString("test"),
				JsonFalse
			)
		}
		"work with objects" in {
			jsonObject("a" -> 1, "b" -> "x", "c" -> JsonNull)	mustEqual
			JsonObject.Var(
				"a"	-> JsonNumber(1),
				"b"	-> JsonString("x"),
				"c"	-> JsonNull
			)
		}
	}
}
