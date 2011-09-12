package scjson.codec

import scjson._

object JSEncoderNice {
	/** unparse a JSValue into a String */ 
	def write(v:JSValue):String	= v match {
		case JSNull			=> "null"
		case JSTrue			=> "true"
		case JSFalse		=> "false"
		case JSNumber(data)	=> data.toString
		case JSString(data)	=> data map writeChar mkString("\"","","\"")
		// case JSArray(data)	=> data map write mkString("[", ",", "]")
		// case JSObject(data)	=> data.iterator map { case (key,value)	=> write(key) + ":" + write(value) } mkString("{", ",", "}")
		case JSArray(data)	=> {
			val inner	= data map write mkString ",\n"
			if (inner.nonEmpty)	"[\n" + inner.replaceAll("(?m)^", "\t") + "\n]"
			else				"[]"
		}
		case JSObject(data)	=> {
			val inner	= data.iterator map { case (key,value)	=> write(key) + ":\t" + write(value) } mkString ",\n"
			if (inner.nonEmpty)	"{\n" + inner.replaceAll("(?m)^", "\t") + "\n}"
			else				"{}"
		}
	}
	
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
