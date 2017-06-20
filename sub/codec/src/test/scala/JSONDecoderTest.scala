package scjson.codec

import org.specs2.mutable._

import scutil.lang._

import scjson.ast._

class JSONDecoderTest extends Specification {
	"JSONDecoderTest" should {
		"decode null" in {
			(JSONCodec decode "null") mustEqual Right(JSONNull)
		}
		"decode true" in {
			(JSONCodec decode "true") mustEqual Right(JSONTrue)
		}
		"decode false" in {
			(JSONCodec decode "false") mustEqual Right(JSONFalse)
		}
		
		"decode int 0" in {
			(JSONCodec decode "0") mustEqual Right(JSONNumber(0))
		}
		"decode int -1" in {
			(JSONCodec decode "-1") mustEqual Right(JSONNumber(-1))
		}
		"decode int 1" in {
			(JSONCodec decode "1") mustEqual Right(JSONNumber(1))
		}
		
		"fail with a single dot" in {
			JSONCodec decode "." must beLeft
		}
		"fail without fraction after dot" in {
			JSONCodec decode "0." must beLeft
		}
		"fail without int before fraction" in {
			JSONCodec decode ".0" must beLeft
		}
		"fail with a single dot before exp" in {
			JSONCodec decode ".e1" must beLeft
		}
		"fail without int before exp" in {
			JSONCodec decode "e1" must beLeft
		}
		"fail without digits after exp" in {
			JSONCodec decode "1e" must beLeft
		}
		
		"decode float 0.0" in {
			(JSONCodec decode "0.0") mustEqual Right(JSONNumber(0))
		}
		"decode float 1e1" in {
			(JSONCodec decode "1e1") mustEqual Right(JSONNumber(10))
		}
		"decode float 2e+3" in {
			(JSONCodec decode "2e+3") mustEqual Right(JSONNumber(2000))
		}
		"decode float 10e-1" in {
			(JSONCodec decode "10e-1") mustEqual Right(JSONNumber(1))
		}
		"decode float 47.11" in {
			(JSONCodec decode "47.11") mustEqual Right(JSONNumber(47.11))
		}
		
		"fail with a leading zero in the body" in {
			// (JSONCodec decode "00") must beLike { case Fail(_) => ok }
			JSONCodec decode "00" must beLeft
		}
		"allow a leading zero in the exponent" in {
			(JSONCodec decode "0E00") mustEqual Right(JSONNumber(0))
		}
	
		"decode simple strings" in {
			(JSONCodec decode "\"hallo, welt!\"") mustEqual Right(JSONString("hallo, welt!"))
		}
		"decode string escapes" in {
			(JSONCodec decode "\" \\\\ \\/ \\t \\r \\n \\f \\b \"") mustEqual Right(JSONString(" \\ / \t \r \n \f \b "))
		}
		"decode small hex escapes" in {
			(JSONCodec decode "\" \\u0123 \"") mustEqual Right(JSONString(" \u0123 "))
		}
		"decode big hex escapes" in {
			(JSONCodec decode "\" \\uf3e2 \"") mustEqual Right(JSONString(" \uf3e2 "))
		}
		"decode upper case hex escapes" in {
			(JSONCodec decode "\" \\uBEEF \"") mustEqual Right(JSONString(" \uBEEF "))
		}
		"decode hex escapes outside the basic plane" in {
			(JSONCodec decode "\"\\uD834\\uDD1E\"") mustEqual Right(JSONString("\uD834\uDD1E"))
		}
		"decode hex escapes outside the basic plane" in {
			val cs	= new java.lang.StringBuilder appendCodePoint 0x1D11E toString;
			(JSONCodec decode "\"\\uD834\\uDD1E\"") mustEqual Right(JSONString(cs))
		}
		
		"decode arrays with 0 elements" in {
			(JSONCodec decode "[]") mustEqual Right(JSONArray(ISeq()))
		}
		"decode arrays with 1 elements" in {
			(JSONCodec decode "[1]") mustEqual Right(JSONArray(ISeq(JSONNumber(1))))
		}
		"decode arrays with 2 elements" in {
			(JSONCodec decode "[1,2]") mustEqual Right(JSONArray(ISeq(JSONNumber(1),JSONNumber(2))))
		}
		
		"allow legal whitespace in arrays" in {
			(JSONCodec decode "[ ]") mustEqual Right(JSONArray(ISeq()))
		}
		"disallow illegal whitespace in arrays" in {
			JSONCodec decode "[\u00a0]" must beLeft
		}
		
		"decode objects with 0 elements" in {
			(JSONCodec decode "{}") mustEqual Right(JSONObject.empty)
		}
		"decode objects with 1 elements" in {
			(JSONCodec decode "{\"a\":1}") mustEqual Right(JSONObject(ISeq("a"->JSONNumber(1))))
		}
		"decode objects with 2 elements" in {
			(JSONCodec decode "{\"a\":1,\"b\":2}") mustEqual Right(JSONObject(ISeq("a"->JSONNumber(1),"b"->JSONNumber(2))))
		}
	}
}
