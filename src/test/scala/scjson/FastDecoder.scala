package scjson.serialization

import org.specs._
import org.specs.matcher._

import scutil.Implicits._

import scjson._
import scjson.codec._

class FastDecoder extends Specification {
	"FastDecoder" should {
		"decode null" in {
			(JSDecoderFast read "null") mustEqual Some(JSNull)
		}
		"decode true" in {
			(JSDecoderFast read "true") mustEqual Some(JSTrue)
		}
		"decode false" in {
			(JSDecoderFast read "false") mustEqual Some(JSFalse)
		}
		
		"decode int 0" in {
			(JSDecoderFast read "0") mustEqual Some(JSNumber(0))
		}
		"decode int -1" in {
			(JSDecoderFast read "-1") mustEqual Some(JSNumber(-1))
		}
		"decode int 1" in {
			(JSDecoderFast read "1") mustEqual Some(JSNumber(1))
		}
		"decode int 0." in {
			(JSDecoderFast read "0.") mustEqual Some(JSNumber(0))
		}
		"decode int .0" in {
			(JSDecoderFast read ".0") mustEqual Some(JSNumber(0))
		}
		"decode int 0.0" in {
			(JSDecoderFast read "0.0") mustEqual Some(JSNumber(0))
		}
		"not decode 1e" in {
			(JSDecoderFast read "1e") mustEqual None
		}
		"decode 1e1" in {
			(JSDecoderFast read "1e1") mustEqual Some(JSNumber(10))
		}
		"decode 2e+3" in {
			(JSDecoderFast read "2e+3") mustEqual Some(JSNumber(2000))
		}
		"decode 10e-1" in {
			(JSDecoderFast read "10e-1") mustEqual Some(JSNumber(1))
		}
		"decode 47.11" in {
			(JSDecoderFast read "47.11") mustEqual Some(JSNumber(47.11))
		}
	
		"decode simple strings" in {
			(JSDecoderFast read "\"hallo, welt!\"") mustEqual Some(JSString("hallo, welt!"))
		}
		"decode string escapes" in {
			(JSDecoderFast read "\" \\\\ \\/ \\t \\r \\n \\f \\b \"") mustEqual Some(JSString(" \\ / \t \r \n \f \b "))
		}
		"decode small hex escapes" in {
			(JSDecoderFast read "\" \\u0123 \"") mustEqual Some(JSString(" \u0123 "))
		}
		"decode big hex escapes" in {
			(JSDecoderFast read "\" \\uf3e2 \"") mustEqual Some(JSString(" \uf3e2 "))
		}
		
		"decode arrays with 0 elements" in {
			(JSDecoderFast read "[]") mustEqual Some(JSArray(Seq()))
		}
		"decode arrays with 1 elements" in {
			(JSDecoderFast read "[1]") mustEqual Some(JSArray(Seq(JSNumber(1))))
		}
		"decode arrays with 2 elements" in {
			(JSDecoderFast read "[1,2]") mustEqual Some(JSArray(Seq(JSNumber(1),JSNumber(2))))
		}
		
		"decode objects with 0 elements" in {
			(JSDecoderFast read "{}") mustEqual Some(JSObject(Map()))
		}
		"decode objects with 1 elements" in {
			(JSDecoderFast read "{\"a\":1}") mustEqual Some(JSObject(Map(JSString("a")->JSNumber(1))))
		}
		"decode objects with 2 elements" in {
			(JSDecoderFast read "{\"a\":1,\"b\":2}") mustEqual Some(JSObject(Map(JSString("a")->JSNumber(1),JSString("b")->JSNumber(2))))
		}
	}
}
