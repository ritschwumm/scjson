package scjson.codec

import scutil.lang._
import scutil.tried._

import scjson._

object JSONCodec {
	/** unparse a JSONValue into a String */
	def encode(it:JSONValue):String	=
			JSONEncoder encode (it, false)
		
	/** unparse a JSONValue into a String */
	def encodePretty(it:JSONValue):String	=
			JSONEncoder encode (it, true)
		
	/** parse a JSON formatted String into a JSONValue */
	def decode(it:String):Tried[JSONDecodeException,JSONValue]	=
			JSONDecoder decode it
		
	/** small-style marshaller */
	def asMarshaller:Marshaller[JSONValue,String]	=
			Marshaller[JSONValue,String](
					encode,
					decode _ andThen { _.toOption })
}
