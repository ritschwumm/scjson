package scjson.codec

import scutil.lang._

import scjson.ast._

object JsonCodec {
	def encodeShort(it:JsonValue):String	=
			encode(it, false)
		
	def encodePretty(it:JsonValue):String	=
			encode(it, true)
		
	/** unparse a JsonValue into a String */
	def encode(it:JsonValue, pretty:Boolean):String	=
			JsonEncoder encode (it, pretty)
		
	/** parse a Json formatted String into a JsonValue */
	def decode(it:String):Either[JsonDecodeFailure,JsonValue]	=
			JsonDecoder decode it
		
	/** small-style */
	def asPrism(pretty:Boolean):Prism[String,JsonValue]	=
			Prism(
				decode _ andThen { _.toOption },
				encode(_, pretty)
			)
}
