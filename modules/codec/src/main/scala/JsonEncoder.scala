package scjson.codec

import scjson.ast._

private object JsonEncoder {
	/** unparse a JsonValue into a String */
	def encode(v:JsonValue, pretty:Boolean):String	= {
		val encoder	= new JsonEncoder(pretty)
		encoder encode v
		encoder.result
	}

	private val indention	= "\t"
	private val hexTable	= "0123456789abcdef".toCharArray
}

private final class JsonEncoder(pretty:Boolean) {
	private val b		= new StringBuilder
	private var level	= 0

	def result:String	= b.toString

	/** unparse a JsonValue into a String */
	private def encode(v:JsonValue):Unit	=
		v match {
			case JsonNull			=> b append "null"
			case JsonTrue			=> b append "true"
			case JsonFalse			=> b append "false"
			case JsonNumber(data)	=> b append  data.toString
			case JsonString(data)	=> encodeString(data)
			case JsonArray(data)	=>
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
			case JsonObject(data)	=>
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
						b	++= JsonEncoder.indention
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

	private def encodeString(data:String):Unit	= {
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
				b	+= JsonEncoder hexTable ((c >> 12) & 0xf)
				b	+= JsonEncoder hexTable ((c >>  8) & 0xf)
				b	+= JsonEncoder hexTable ((c >>  4) & 0xf)
				b	+= JsonEncoder hexTable ((c >>  0) & 0xf)
			case c 	=> b += c
		}
		i += 1
		}
		b += '"'
	}

	private def indent():Unit	= {
		var x	= 0
		while (x < level) {
		b	++= JsonEncoder.indention
		x	+= 1
		}
	}
}
