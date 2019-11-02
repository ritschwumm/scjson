package scjson.codec

import org.specs2.mutable._

import scjson.ast._

class JsonDecoderTest extends Specification {
	"JsonDecoderTest" should {
		"decode null" in {
			(JsonCodec decode "null") mustEqual Right(JsonNull)
		}
		"decode true" in {
			(JsonCodec decode "true") mustEqual Right(JsonTrue)
		}
		"decode false" in {
			(JsonCodec decode "false") mustEqual Right(JsonFalse)
		}

		"decode int 0" in {
			(JsonCodec decode "0") mustEqual Right(JsonNumber(0))
		}
		"decode int -1" in {
			(JsonCodec decode "-1") mustEqual Right(JsonNumber(-1))
		}
		"decode int 1" in {
			(JsonCodec decode "1") mustEqual Right(JsonNumber(1))
		}

		"fail with a single dot" in {
			JsonCodec decode "." must beLeft
		}
		"fail without fraction after dot" in {
			JsonCodec decode "0." must beLeft
		}
		"fail without int before fraction" in {
			JsonCodec decode ".0" must beLeft
		}
		"fail with a single dot before exp" in {
			JsonCodec decode ".e1" must beLeft
		}
		"fail without int before exp" in {
			JsonCodec decode "e1" must beLeft
		}
		"fail without digits after exp" in {
			JsonCodec decode "1e" must beLeft
		}

		"decode float 0.0" in {
			(JsonCodec decode "0.0") mustEqual Right(JsonNumber(0))
		}
		"decode float 1e1" in {
			(JsonCodec decode "1e1") mustEqual Right(JsonNumber(10))
		}
		"decode float 2e+3" in {
			(JsonCodec decode "2e+3") mustEqual Right(JsonNumber(2000))
		}
		"decode float 10e-1" in {
			(JsonCodec decode "10e-1") mustEqual Right(JsonNumber(1))
		}
		"decode float 47.11" in {
			(JsonCodec decode "47.11") mustEqual Right(JsonNumber(47.11))
		}

		"fail with a leading zero in the body" in {
			// (JsonCodec decode "00") must beLike { case Fail(_) => ok }
			JsonCodec decode "00" must beLeft
		}
		"allow a leading zero in the exponent" in {
			(JsonCodec decode "0E00") mustEqual Right(JsonNumber(0))
		}

		"decode simple strings" in {
			(JsonCodec decode "\"hallo, welt!\"") mustEqual Right(JsonString("hallo, welt!"))
		}
		"decode string escapes" in {
			(JsonCodec decode "\" \\\\ \\/ \\t \\r \\n \\f \\b \"") mustEqual Right(JsonString(" \\ / \t \r \n \f \b "))
		}
		"decode small hex escapes" in {
			(JsonCodec decode "\" \\u0123 \"") mustEqual Right(JsonString(" \u0123 "))
		}
		"decode big hex escapes" in {
			(JsonCodec decode "\" \\uf3e2 \"") mustEqual Right(JsonString(" \uf3e2 "))
		}
		"decode upper case hex escapes" in {
			(JsonCodec decode "\" \\uBEEF \"") mustEqual Right(JsonString(" \uBEEF "))
		}
		"decode hex escapes outside the basic plane" in {
			(JsonCodec decode "\"\\uD834\\uDD1E\"") mustEqual Right(JsonString("\uD834\uDD1E"))
		}
		"decode hex escapes outside the basic plane" in {
			val cs	= new java.lang.StringBuilder appendCodePoint 0x1D11E toString;
			(JsonCodec decode "\"\\uD834\\uDD1E\"") mustEqual Right(JsonString(cs))
		}

		"decode arrays with 0 elements" in {
			(JsonCodec decode "[]") mustEqual Right(JsonArray(Seq()))
		}
		"decode arrays with 1 elements" in {
			(JsonCodec decode "[1]") mustEqual Right(JsonArray(Seq(JsonNumber(1))))
		}
		"decode arrays with 2 elements" in {
			(JsonCodec decode "[1,2]") mustEqual Right(JsonArray(Seq(JsonNumber(1),JsonNumber(2))))
		}

		"allow legal whitespace in arrays" in {
			(JsonCodec decode "[ ]") mustEqual Right(JsonArray(Seq()))
		}
		"disallow illegal whitespace in arrays" in {
			JsonCodec decode "[\u00a0]" must beLeft
		}

		"decode objects with 0 elements" in {
			(JsonCodec decode "{}") mustEqual Right(JsonObject.empty)
		}
		"decode objects with 1 elements" in {
			(JsonCodec decode "{\"a\":1}") mustEqual Right(JsonObject(Seq("a"->JsonNumber(1))))
		}
		"decode objects with 2 elements" in {
			(JsonCodec decode "{\"a\":1,\"b\":2}") mustEqual Right(JsonObject(Seq("a"->JsonNumber(1),"b"->JsonNumber(2))))
		}
	}
}
