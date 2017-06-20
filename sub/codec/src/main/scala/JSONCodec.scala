package scjson.codec

import scutil.lang._

import scjson.ast._

object JSONCodec {
	def encodeShort(it:JSONValue):String	=
			encode(it, false)
		
	def encodePretty(it:JSONValue):String	=
			encode(it, true)
		
	/** unparse a JSONValue into a String */
	def encode(it:JSONValue, pretty:Boolean):String	=
			JSONEncoder encode (it, pretty)
		
	/** parse a JSON formatted String into a JSONValue */
	def decode(it:String):Either[JSONDecodeFailure,JSONValue]	=
			JSONDecoder decode it
		
	/** small-style */
	def asPrism(pretty:Boolean):Prism[String,JSONValue]	=
			Prism(
				decode _ andThen { _.toOption },
				encode(_, pretty)
			)
}
