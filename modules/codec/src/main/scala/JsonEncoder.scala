package scjson.codec

import scjson.ast._

private object JsonEncoder {
	/** unparse a JsonValue into a String */
	def encode(v:JsonValue, pretty:Boolean):String	= {
		val builder	= new StringBuilder
		val encoder	= new JsonEncoder(builder, pretty)
		v.visit(encoder)
		builder.toString
	}

	private val indention	= "\t"
	private val hexTable	= "0123456789abcdef".toCharArray
}

private final class JsonEncoder(b:StringBuilder, pretty:Boolean) extends JsonVisitor[Unit] {
	private var level	= 0

	def onNull():Unit					= b append "null"
	def onBoolean(value:Boolean):Unit	= if (value) b append "true" else b append "false"
	def onNumber(value:BigDecimal):Unit	= b append value.toString
	def onString(value:String):Unit		= encodeString(value)

	def onArray(items:Seq[JsonValue]):Unit	=
		if (items.isEmpty) {
			b	+= '['
			b	+= ']'
		}
		else if (pretty) {
			b		+= '['
			level	+= 1
			var sep	= false
			items foreach { it =>
				if (sep)	b += ','
				else		sep	= true
				b	+= '\n';	indent()
				it.visit(this)
			}
			level	-= 1
			b	+= '\n';	indent()
			b	+= ']'
		}
		else {
			b	+= '['
			var sep	= false
			items foreach { it =>
				if (sep)	b += ','
				else		sep	= true
				it.visit(this)
			}
			b	+= ']'
		}

	def onObject(fields:Seq[(String, JsonValue)]):Unit	=
		if (fields.isEmpty) {
			b	+= '{'
			b	+= '}'
		}
		else if (pretty) {
			b	+= '{'
			level	+= 1
			var sep	= false
			fields foreach { case (key, value) =>
				if (sep)	b += ','
				else		sep	= true
				b	+= '\n';	indent()
				encodeString(key)
				b	+= ':'
				b	++= JsonEncoder.indention
				value.visit(this)
			}
			level	-= 1
			b	+= '\n';	indent()
			b	+= '}'
		}
		else {
			b	+= '{'
			var sep	= false
			fields foreach { case (key, value) =>
				if (sep)	b += ','
				else		sep	= true
				encodeString(key)
				b	+= ':'
				value.visit(this)
			}
			b	+= '}'
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
