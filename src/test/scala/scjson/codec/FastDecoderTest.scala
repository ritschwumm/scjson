package scjson.codec

import org.specs2.mutable._

import scutil.Implicits._
import scutil.tried._

import scjson._
import scjson.codec._

class FastDecoderTest extends Specification {
	"FastDecoder" should {
		"decode null" in {
			(JSONDecoderFast read "null") mustEqual Win(JSONNull)
		}
		"decode true" in {
			(JSONDecoderFast read "true") mustEqual Win(JSONTrue)
		}
		"decode false" in {
			(JSONDecoderFast read "false") mustEqual Win(JSONFalse)
		}
		
		"decode int 0" in {
			(JSONDecoderFast read "0") mustEqual Win(JSONNumber(0))
		}
		"decode int -1" in {
			(JSONDecoderFast read "-1") mustEqual Win(JSONNumber(-1))
		}
		"decode int 1" in {
			(JSONDecoderFast read "1") mustEqual Win(JSONNumber(1))
		}
		"decode int 0." in {
			(JSONDecoderFast read "0.") mustEqual Win(JSONNumber(0))
		}
		"decode int .0" in {
			(JSONDecoderFast read ".0") mustEqual Win(JSONNumber(0))
		}
		"decode int 0.0" in {
			(JSONDecoderFast read "0.0") mustEqual Win(JSONNumber(0))
		}
		"not decode 1e" in {
			(JSONDecoderFast read "1e").toEither must beLeft
		}
		"decode 1e1" in {
			(JSONDecoderFast read "1e1") mustEqual Win(JSONNumber(10))
		}
		"decode 2e+3" in {
			(JSONDecoderFast read "2e+3") mustEqual Win(JSONNumber(2000))
		}
		"decode 10e-1" in {
			(JSONDecoderFast read "10e-1") mustEqual Win(JSONNumber(1))
		}
		"decode 47.11" in {
			(JSONDecoderFast read "47.11") mustEqual Win(JSONNumber(47.11))
		}
	
		"decode simple strings" in {
			(JSONDecoderFast read "\"hallo, welt!\"") mustEqual Win(JSONString("hallo, welt!"))
		}
		"decode string escapes" in {
			(JSONDecoderFast read "\" \\\\ \\/ \\t \\r \\n \\f \\b \"") mustEqual Win(JSONString(" \\ / \t \r \n \f \b "))
		}
		"decode small hex escapes" in {
			(JSONDecoderFast read "\" \\u0123 \"") mustEqual Win(JSONString(" \u0123 "))
		}
		"decode big hex escapes" in {
			(JSONDecoderFast read "\" \\uf3e2 \"") mustEqual Win(JSONString(" \uf3e2 "))
		}
		"decode upper case hex escapes" in {
			(JSONDecoderFast read "\" \\uBEEF \"") mustEqual Win(JSONString(" \uBEEF "))
		}
		
		"decode arrays with 0 elements" in {
			(JSONDecoderFast read "[]") mustEqual Win(JSONArray(Seq()))
		}
		"decode arrays with 1 elements" in {
			(JSONDecoderFast read "[1]") mustEqual Win(JSONArray(Seq(JSONNumber(1))))
		}
		"decode arrays with 2 elements" in {
			(JSONDecoderFast read "[1,2]") mustEqual Win(JSONArray(Seq(JSONNumber(1),JSONNumber(2))))
		}
		
		"decode objects with 0 elements" in {
			(JSONDecoderFast read "{}") mustEqual Win(JSONObject.empty)
		}
		"decode objects with 1 elements" in {
			(JSONDecoderFast read "{\"a\":1}") mustEqual Win(JSONObject(Seq("a"->JSONNumber(1))))
		}
		"decode objects with 2 elements" in {
			(JSONDecoderFast read "{\"a\":1,\"b\":2}") mustEqual Win(JSONObject(Seq("a"->JSONNumber(1),"b"->JSONNumber(2))))
		}
	}
}
