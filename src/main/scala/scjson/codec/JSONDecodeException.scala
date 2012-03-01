package scjson.codec

import scala.collection.mutable

import scjson._

/** the input is invalid */
class JSONDecodeException(input:String, offset:Int, expectation:String) extends Exception {
	override def getMessage:String	= 
			"at offset: " + offset + " expected: " + expectation

	def lookingAt:String	= {
		val width	= 80
		val end		= (offset+width) min input.length
		input substring (offset, end) replaceAll ("[\r\n]", "§")
	}
}
