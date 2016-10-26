package scjson.pickle.syntax

import org.specs2.mutable._

import scjson.ast._
import scjson.pickle.protocol.StandardProtocol._

class SyntaxTest extends Specification {
	"syntax should" should {
		"work with simple values" in {
			jsonSimple(1) mustEqual JSONNumber(1)
		}
		"work with arrays" in {
			jsonArray(1, "test", false) mustEqual
			JSONVarArray(
				JSONNumber(1),
				JSONString("test"),
				JSONFalse
			)
		}
		"work with objects" in {
			jsonObject("a" -> 1, "b" -> "x", "c" -> JSONNull)	mustEqual
			JSONVarObject(
				"a"	-> JSONNumber(1),
				"b"	-> JSONString("x"),
				"c"	-> JSONNull
			)
		}
	}
}
