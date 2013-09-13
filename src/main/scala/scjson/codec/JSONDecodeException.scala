package scjson.codec

import scjson.JSONInputException

/** the input is invalid */
final class JSONDecodeException(
	input:String, offset:Int, expectation:String
)
extends JSONInputException(
	"at offset: " + offset + " expected: " + expectation
) {
	def lookingAt:String	= {
		val width	= 80
		val end		= (offset+width) min input.length
		input substring (offset, end) replaceAll ("[\r\n]", "§")
	}
}
