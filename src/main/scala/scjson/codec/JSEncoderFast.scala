package scjson.codec

import scjson._

object JSEncoderFast {
	/** unparse a JSValue into a String */ 
	def write(v:JSValue):String	= {
		val	b	= new StringBuilder
		write1(v, b)
		b.toString
	}
	
	private val hexTable	= "0123456789abcdef".toCharArray
	
	/** unparse a JSValue into a String */ 
	private def write1(v:JSValue, b:StringBuilder):Unit	= v match {
		case JSNull			=> b append "null"
		case JSTrue			=> b append "true"
		case JSFalse		=> b append "false"
		case JSNumber(data)	=> b append  data.toString
		case JSString(data)	=> {
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
						b	+= '\\'; b += 'u'
						b	+=  hexTable((c >> 12) & 0xf)
						b	+=  hexTable((c >>  8) & 0xf)
						b	+=  hexTable((c >>  4) & 0xf)
						b	+=  hexTable((c >>  0) & 0xf)
					case c 	=> b += c
				}
				i += 1
			}
			b += '"'
		}
		case JSArray(data)	=> 
			b	+= '['
			var sep	= false
			data foreach { it =>
				if (sep)	b += ','
				else		sep	= true
				write1(it, b) 
			}
			b	+= ']'
		case JSObject(data)	=> 
			b	+= '{'
			var sep	= false
			data foreach { case (key,value) =>
				if (sep)	b += ','
				else		sep	= true
				write1(key, b) 
				b	+= ':'
				write1(value, b) 
			}
			b	+= '}'
	}
}
