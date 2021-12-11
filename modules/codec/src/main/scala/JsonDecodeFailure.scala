package scjson.codec

import scutil.lang.implicits.*

/** the input is invalid */
final case class JsonDecodeFailure(input:String, offset:Int, expectation:String) {
	def message:String	=
		show"expected $expectation at $offset"

	def lookingAt:String	= {
		val width	= 80
		val end		= (offset+width) min input.length
		input.substring(offset, end).replaceAll("[\r\n]", "§")
	}
}
