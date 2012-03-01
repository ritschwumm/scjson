package scjson

import scutil.Marshaller

import scjson.codec._
	
object JSONMarshaller extends Marshaller[JSONValue,String] {
	/** parse a JSON formatted String into a JSONValue */
	def read(s:String):Option[JSONValue]	= JSONDecoderFast read s
	
	/** unparse a JSONValue into a String */ 
	def write(v:JSONValue):String			= JSONEncoderFast write v
}
