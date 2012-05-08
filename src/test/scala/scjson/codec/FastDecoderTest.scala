package scjson.codec

import org.specs2.mutable._

import scutil.Implicits._

import scjson._
import scjson.codec._

class FastDecoderTest extends Specification {
	"FastDecoder" should {
		"decode null" in {
			(JSONDecoderFast read "null") mustEqual Right(JSONNull)
		}
		"decode true" in {
			(JSONDecoderFast read "true") mustEqual Right(JSONTrue)
		}
		"decode false" in {
			(JSONDecoderFast read "false") mustEqual Right(JSONFalse)
		}
		
		"decode int 0" in {
			(JSONDecoderFast read "0") mustEqual Right(JSONNumber(0))
		}
		"decode int -1" in {
			(JSONDecoderFast read "-1") mustEqual Right(JSONNumber(-1))
		}
		"decode int 1" in {
			(JSONDecoderFast read "1") mustEqual Right(JSONNumber(1))
		}
		"decode int 0." in {
			(JSONDecoderFast read "0.") mustEqual Right(JSONNumber(0))
		}
		"decode int .0" in {
			(JSONDecoderFast read ".0") mustEqual Right(JSONNumber(0))
		}
		"decode int 0.0" in {
			(JSONDecoderFast read "0.0") mustEqual Right(JSONNumber(0))
		}
		"not decode 1e" in {
			(JSONDecoderFast read "1e") must beLeft
		}
		"decode 1e1" in {
			(JSONDecoderFast read "1e1") mustEqual Right(JSONNumber(10))
		}
		"decode 2e+3" in {
			(JSONDecoderFast read "2e+3") mustEqual Right(JSONNumber(2000))
		}
		"decode 10e-1" in {
			(JSONDecoderFast read "10e-1") mustEqual Right(JSONNumber(1))
		}
		"decode 47.11" in {
			(JSONDecoderFast read "47.11") mustEqual Right(JSONNumber(47.11))
		}
	
		"decode simple strings" in {
			(JSONDecoderFast read "\"hallo, welt!\"") mustEqual Right(JSONString("hallo, welt!"))
		}
		"decode string escapes" in {
			(JSONDecoderFast read "\" \\\\ \\/ \\t \\r \\n \\f \\b \"") mustEqual Right(JSONString(" \\ / \t \r \n \f \b "))
		}
		"decode small hex escapes" in {
			(JSONDecoderFast read "\" \\u0123 \"") mustEqual Right(JSONString(" \u0123 "))
		}
		"decode big hex escapes" in {
			(JSONDecoderFast read "\" \\uf3e2 \"") mustEqual Right(JSONString(" \uf3e2 "))
		}
		
		"decode arrays with 0 elements" in {
			(JSONDecoderFast read "[]") mustEqual Right(JSONArray(Seq()))
		}
		"decode arrays with 1 elements" in {
			(JSONDecoderFast read "[1]") mustEqual Right(JSONArray(Seq(JSONNumber(1))))
		}
		"decode arrays with 2 elements" in {
			(JSONDecoderFast read "[1,2]") mustEqual Right(JSONArray(Seq(JSONNumber(1),JSONNumber(2))))
		}
		
		"decode objects with 0 elements" in {
			(JSONDecoderFast read "{}") mustEqual Right(JSONObject(Map()))
		}
		"decode objects with 1 elements" in {
			(JSONDecoderFast read "{\"a\":1}") mustEqual Right(JSONObject(Map(JSONString("a")->JSONNumber(1))))
		}
		"decode objects with 2 elements" in {
			(JSONDecoderFast read "{\"a\":1,\"b\":2}") mustEqual Right(JSONObject(Map(JSONString("a")->JSONNumber(1),JSONString("b")->JSONNumber(2))))
		}
	}
}
