package scjson

import scala.util.control.Exception._

import scutil.data.Marshaller

import scjson.codec._
	
object JSONMarshaller extends Marshaller[JSONValue,String] {
	/** unparse a JSONValue into a String */ 
	def write(it:JSONValue):String	= 
			JSONEncoderFast write it
	
	/** parse a JSON formatted String into a JSONValue */
	def read(it:String):Option[JSONValue]	=
			(JSONDecoderFast read it).toOption
}
