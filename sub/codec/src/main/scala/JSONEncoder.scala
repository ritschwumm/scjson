package scjson.codec

import scjson.ast._

private object JSONEncoder {
	/** unparse a JSONValue into a String */
	def encode(v:JSONValue, pretty:Boolean):String	= {
		val encoder	= new JSONEncoder(pretty)
		encoder encode v
		encoder.result
	}
	
	private val indention	= "\t"
	private val hexTable	= "0123456789abcdef".toCharArray
}

private final class JSONEncoder(pretty:Boolean) {
	private val b		= new StringBuilder
	private var level	= 0
	
	def result:String	= b.toString
	
	/** unparse a JSONValue into a String */
	private def encode(v:JSONValue) {
		v match {
			case JSONNull			=> b append "null"
			case JSONTrue			=> b append "true"
			case JSONFalse			=> b append "false"
			case JSONNumber(data)	=> b append  data.toString
			case JSONString(data)	=> encodeString(data)
			case JSONArray(data)	=>
				if (data.isEmpty) {
					b	+= '['
					b	+= ']'
				}
				else if (pretty) {
					b		+= '['
					level	+= 1
					var sep	= false
					data foreach { it =>
						if (sep)	b += ','
						else		sep	= true
						b	+= '\n';	indent()
						encode(it)
					}
					level	-= 1
					b	+= '\n';	indent()
					b	+= ']'
				}
				else {
					b	+= '['
					var sep	= false
					data foreach { it =>
						if (sep)	b += ','
						else		sep	= true
						encode(it)
					}
					b	+= ']'
				}
			case JSONObject(data)	=>
				if (data.isEmpty) {
					b	+= '{'
					b	+= '}'
				}
				else if (pretty) {
					b	+= '{'
					level	+= 1
					var sep	= false
					data foreach { case (key, value) =>
						if (sep)	b += ','
						else		sep	= true
						b	+= '\n';	indent()
						encodeString(key)
						b	+= ':'
						b	++= JSONEncoder.indention
						encode(value)
					}
					level	-= 1
					b	+= '\n';	indent()
					b	+= '}'
				}
				else {
					b	+= '{'
					var sep	= false
					data foreach { case (key, value) =>
						if (sep)	b += ','
						else		sep	= true
						encodeString(key)
						b	+= ':'
						encode(value)
					}
					b	+= '}'
				}
		}
	}
	
	private def encodeString(data:String) {
		b += '"'
		var i	= 0
		while (i < data.length) {
			data charAt i match {
				case '\"' 	=> b += '\\'; b += '\"'
				case '\\'	=> b += '\\'; b += '\\'
				case '\b'	=> b += '\\'; b += 'b'
				case '\f'	=> b += '\\'; b += 'f'
				case '\n'	=> b += '\\'; b += 'n'
				case '\r'	=> b += '\\'; b += 'r'
				case '\t'	=> b += '\\'; b += 't'
				case c if c < 32	=>
					b	+= '\\'; b	+= 'u'
					b	+= JSONEncoder hexTable ((c >> 12) & 0xf)
					b	+= JSONEncoder hexTable ((c >>  8) & 0xf)
					b	+= JSONEncoder hexTable ((c >>  4) & 0xf)
					b	+= JSONEncoder hexTable ((c >>  0) & 0xf)
				case c 	=> b += c
			}
			i += 1
		}
		b += '"'
	}
	
	private def indent() {
		var x	= 0
		while (x < level) {
			b	++= JSONEncoder.indention
			x	+= 1
		}
	}
}
