package scjson.codec

import scutil.lang._

import scjson.ast._

object JSONCodec {
	/** unparse a JSONValue into a String */
	def encode(it:JSONValue):String	=
			JSONEncoder encode (it, false)
		
	/** unparse a JSONValue into a String */
	def encodePretty(it:JSONValue):String	=
			JSONEncoder encode (it, true)
		
	/** parse a JSON formatted String into a JSONValue */
	def decode(it:String):Tried[JSONDecodeFailure,JSONValue]	=
			JSONDecoder decode it
		
	/** small-style */
	def asPrism:Prism[String,JSONValue]	=
			Prism(
				decode _ andThen { _.toOption },
				encode
			)
}
