package scjson.codec

import org.specs2.mutable._

import scutil.implicits._
import scutil.lang._

import scjson._
import scjson.codec._

class JSONDecoderTest extends Specification {
	"JSONDecoderTest" should {
		"decode null" in {
			(JSONCodec decode "null") mustEqual Win(JSONNull)
		}
		"decode true" in {
			(JSONCodec decode "true") mustEqual Win(JSONTrue)
		}
		"decode false" in {
			(JSONCodec decode "false") mustEqual Win(JSONFalse)
		}
		
		"decode int 0" in {
			(JSONCodec decode "0") mustEqual Win(JSONNumber(0))
		}
		"decode int -1" in {
			(JSONCodec decode "-1") mustEqual Win(JSONNumber(-1))
		}
		"decode int 1" in {
			(JSONCodec decode "1") mustEqual Win(JSONNumber(1))
		}
		"decode int 0." in {
			(JSONCodec decode "0.") mustEqual Win(JSONNumber(0))
		}
		"decode int .0" in {
			(JSONCodec decode ".0") mustEqual Win(JSONNumber(0))
		}
		"decode int 0.0" in {
			(JSONCodec decode "0.0") mustEqual Win(JSONNumber(0))
		}
		"not decode 1e" in {
			(JSONCodec decode "1e").toEither must beLeft
		}
		"decode 1e1" in {
			(JSONCodec decode "1e1") mustEqual Win(JSONNumber(10))
		}
		"decode 2e+3" in {
			(JSONCodec decode "2e+3") mustEqual Win(JSONNumber(2000))
		}
		"decode 10e-1" in {
			(JSONCodec decode "10e-1") mustEqual Win(JSONNumber(1))
		}
		"decode 47.11" in {
			(JSONCodec decode "47.11") mustEqual Win(JSONNumber(47.11))
		}
		
		"fail with a leading zero in the body" in {
			(JSONCodec decode "00") must beLike { case Fail(_) => ok }
		}
		"allow a leading zero in the exponent" in {
			(JSONCodec decode "0E00") mustEqual Win(JSONNumber(0))
		}
	
		"decode simple strings" in {
			(JSONCodec decode "\"hallo, welt!\"") mustEqual Win(JSONString("hallo, welt!"))
		}
		"decode string escapes" in {
			(JSONCodec decode "\" \\\\ \\/ \\t \\r \\n \\f \\b \"") mustEqual Win(JSONString(" \\ / \t \r \n \f \b "))
		}
		"decode small hex escapes" in {
			(JSONCodec decode "\" \\u0123 \"") mustEqual Win(JSONString(" \u0123 "))
		}
		"decode big hex escapes" in {
			(JSONCodec decode "\" \\uf3e2 \"") mustEqual Win(JSONString(" \uf3e2 "))
		}
		"decode upper case hex escapes" in {
			(JSONCodec decode "\" \\uBEEF \"") mustEqual Win(JSONString(" \uBEEF "))
		}
		"decode hex escapes outside the basic plane" in {
			(JSONCodec decode "\"\\uD834\\uDD1E\"") mustEqual Win(JSONString("\uD834\uDD1E"))
		}
		"decode hex escapes outside the basic plane" in {
			val cs	= new java.lang.StringBuilder appendCodePoint 0x1D11E toString;
			(JSONCodec decode "\"\\uD834\\uDD1E\"") mustEqual Win(JSONString(cs))
		}
		
		"decode arrays with 0 elements" in {
			(JSONCodec decode "[]") mustEqual Win(JSONArray(Seq()))
		}
		"decode arrays with 1 elements" in {
			(JSONCodec decode "[1]") mustEqual Win(JSONArray(Seq(JSONNumber(1))))
		}
		"decode arrays with 2 elements" in {
			(JSONCodec decode "[1,2]") mustEqual Win(JSONArray(Seq(JSONNumber(1),JSONNumber(2))))
		}
		
		"allow legal whitespace in arrays" in {
			(JSONCodec decode "[ ]") mustEqual Win(JSONArray(Seq()))
		}
		"disallow illegal whitespace in arrays" in {
			(JSONCodec decode "[ ]") must beLike { case Fail(_) => ok }
		}
		
		"decode objects with 0 elements" in {
			(JSONCodec decode "{}") mustEqual Win(JSONObject.empty)
		}
		"decode objects with 1 elements" in {
			(JSONCodec decode "{\"a\":1}") mustEqual Win(JSONObject(Seq("a"->JSONNumber(1))))
		}
		"decode objects with 2 elements" in {
			(JSONCodec decode "{\"a\":1,\"b\":2}") mustEqual Win(JSONObject(Seq("a"->JSONNumber(1),"b"->JSONNumber(2))))
		}
	}
}
