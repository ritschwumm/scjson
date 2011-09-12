package scjson

import scutil.Marshaller

import scjson.codec._
	
object JSMarshaller extends Marshaller[JSValue,String] {
	/** parse a JSON formatted String into a JSValue */
	def read(s:String):Option[JSValue]	= JSDecoderFast read s
	
	/** unparse a JSValue into a String */ 
	def write(v:JSValue):String			= JSEncoderFast write v
}
