package scjson.codec

import scjson._

object JSONEncoderNice {
	/** unparse a JSONValue into a String */ 
	def write(v:JSONValue):String	= v match {
		case JSONNull			=> "null"
		case JSONTrue			=> "true"
		case JSONFalse			=> "false"
		case JSONNumber(data)	=> data.toString
		case JSONString(data)	=> writeString(data)
		// case JSONArray(data)	=> data map write mkString("[", ",", "]")
		// case JSONObject(data)	=> data.iterator map { case (key,value)	=> write(key) + ":" + write(value) } mkString("{", ",", "}")
		case JSONArray(data)	=> {
			val inner	= data map write mkString ",\n"
			if (inner.nonEmpty)	"[\n" + inner.replaceAll("(?m)^", "\t") + "\n]"
			else				"[]"
		}
		case JSONObject(data)	=> {
			val inner	= data map { case (key,value)	=> writeString(key) + ":\t" + write(value) } mkString ",\n"
			if (inner.nonEmpty)	"{\n" + inner.replaceAll("(?m)^", "\t") + "\n}"
			else				"{}"
		}
	}
	
	private def writeString(data:String):String	= 
			data map writeChar mkString("\"","","\"")
	
	private def writeChar(char:Char):String	= char match {
		case '"' 	=> "\\\""
		case '\\'	=>	"\\\\"
		// this would be allowed but is ugly
		//case '/'	=> "\\/"
		// these are optional
		case '\b'	=> "\\b"
		case '\f'	=> "\\f"
		case '\n'	=> "\\n"
		case '\r'	=> "\\r"
		case '\t'	=> "\\t"
		case c 
		if c < 32	=> "\\u%04x" format c.toInt
		case c 		=> c.toString
	}
}
