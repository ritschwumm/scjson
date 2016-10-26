package scjson.codec

/** the input is invalid */
final case class JSONDecodeFailure(input:String, offset:Int, expectation:String) {
	def lookingAt:String	= {
		val width	= 80
		val end		= (offset+width) min input.length
		input substring (offset, end) replaceAll ("[\r\n]", "§")
	}
}
