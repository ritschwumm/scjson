package scjson.codec

import scala.collection.immutable

import scjson.ast._

// BETTER unused should be private
 object JsonDecoder {
	/** parse a Json formatted String into a JsonValue */
	def decode(s:String):Either[JsonDecodeFailure,JsonValue]	=
			try {
				Right(new JsonDecoder(s).decode())
			}
			catch { case e:JsonDecodeException =>
				Left(e.failure)
			}
}

private final class JsonDecoder(text:String) {
	val NO_CHAR	= -1
	var	offset	= 0

	private def decode():JsonValue	= {
		val value	= decodeNext()
		ws()
		if (!finished)	throw expected("end of input")
		value
	}

	private def decodeNext():JsonValue = {
		ws()
		if (finished)	throw expected("any char")

		if (isString("null"))	return JsonNull
		if (isString("true"))	return JsonTrue
		if (isString("false"))	return JsonFalse
		if (isChar('[')) {
			val	out	= new immutable.VectorBuilder[JsonValue]
			ws()
			if (isChar(']'))	return JsonArray(out.result)
			while (true) {
				val value	= decodeNext()
				out	+= value
				ws()
				if (isChar(']'))	return JsonArray(out.result)
				if (!isChar(','))	throw expectedClass(",]")
			}
		}
		if (isChar('{')) {
			val out	= new immutable.VectorBuilder[(String,JsonValue)]
			ws()
			if (isChar('}'))	return JsonObject(out.result)
			while (true) {
				val key	= decodeNext() match {
					case JsonString(s)	=> s
					case _				=> throw expected("string key")
				}
				ws();
				if (!isChar(':'))	throw expectedClass(":")
				val value	= decodeNext()
				out	+= (key -> value)
				ws()
				if (isChar('}'))	return JsonObject(out.result)
				if (!isChar(','))	throw expectedClass(",}");
			}
		}
		if (isChar('"')) {
			val out	= new StringBuilder
			while (true) {
				if (isChar('\\')) {
					if (finished)	throw expected("escape continuation")
						 if (isChar('"'))	out	+= '"'
					else if (isChar('\\'))	out	+= '\\'
					else if (isChar('/'))	out	+= '/'
					else if (isChar('t'))	out	+= '\t'
					else if (isChar('r'))	out	+= '\r'
					else if (isChar('n'))	out	+= '\n'
					else if (isChar('f'))	out	+= '\f'
					else if (isChar('b'))	out	+= '\b'
					else if (isChar('u')) {
						if (offset+4 > text.length)	throw expected("4 hex digits")

						val h1	= hexDigit()
						val h2	= hexDigit()
						val h3	= hexDigit()
						val h4	= hexDigit()

						out	+= ((h1 << 12) | (h2 << 8) | (h3 << 4) | (h4 << 0)).toChar
					}
					else throw expectedClass("\"\\/trnfbu")
				}
				else if (isChar('"')) {
					return JsonString(out.result)
				}
				else if (rng('\u0000', '\u001f')) {
					offset	-= 1
					throw expected("not a control character")
				}
				else {
					if (finished)	throw expected("more chars")
					out	+= next.toChar
					consume()
				}
			}
		}

		val before		= offset

		val numNeg		= isChar('-')
		val beforeInt	= offset
		val numInt		= digits()
		if (numNeg || numInt != 0) {
			if (numInt == 0) {
				// TODO [.eE+-n] could issue special errors
				// offset	= beforeBody
				throw expected("number digits")
			}
			if (numInt > 1 && (text charAt beforeInt) == '0') {
				offset	= beforeInt
				throw expected("number digits without leading zero")
			}
			if (isChar('.')) {
				val numFrac	= digits()
				if (numFrac == 0)	throw expected("fraction digits after dot")
			}
			if (isChar('e') || isChar('E')) {
				/*
				val signExpo	=
							 if (isChar('+'))	true
						else if (isChar('-'))	false
						else					true
				*/
				isChar('+') || isChar('-')
				val countExpo	= digits()
				if (countExpo == 0)	throw expected("at least one digit in the exponent")
			}
			try {
				return JsonNumber(BigDecimal(from(before)))
			}
			catch { case e:NumberFormatException	=>
				offset	= before
				throw expected("valid number")
			}
		}

		// no recognized token at all
		offset	= before
		throw expected("unexpected character %04x" format (text charAt offset).toInt)
	}

	private def expected(what:String)	=
			new JsonDecodeException(JsonDecodeFailure(text, offset, what))

	private def expectedClass(charClass:String)	=
			new JsonDecodeException(JsonDecodeFailure(text, offset, "one of " + (JsonCodec encodeShort JsonString(charClass))))

	//-------------------------------------------------------------------------
	//## tokens

	private def hexDigit():Int	= {
		val	c	= text charAt offset
		val h	=
					 if (c >= '0' && c <= '9')	c - '0'
				else if (c >= 'a' && c <= 'f')	c - 'a' + 10
				else if (c >= 'A' && c <= 'F')	c - 'A' + 10
				else throw expected("a hex digit")
		offset	+= 1
		h
	}

	private def digits():Int	= {
		val before	= offset
		var keepOn	= true
		while (!finished && keepOn) {
			val c	= next
			if (c >= '0' && c <= '9')	consume()
			else						keepOn	= false
		}
		offset - before
	}

	private def ws() {
		var keepOn	= true
		while (!finished && keepOn) {
			val c	= next
			if (c == ' '
			|| c == '\t'
			|| c == '\r'
			|| c == '\n')	consume()
			else			keepOn	= false
		}
	}

	private def rng(start:Char, end:Char):Boolean	= {
		val c	= next
			 if (c == NO_CHAR)			false
		else if (c < start || c > end)	false
		else							{ consume(); true }
	}

	private def isChar(c:Char):Boolean	= {
			 if (finished)	false
		else if (next != c)	false
		else				{ consume(); true }
	}

	private def isString(s:String):Boolean	= {
		val end	= offset + s.length
			 if (end > text.length)						false
		else if ((text substring (offset, end)) != s)	false
		else											{ offset	= end; true }
	}

	//-------------------------------------------------------------------------
	//## chars

	private def finished:Boolean	=
			offset == text.length

	private def next:Int	=
			if (finished)	NO_CHAR
			else  			text charAt offset

	/*
	private def previous:Int	=
			if (offset == 0)	NO_CHAR
			else				text charAt offset-1
	*/

	private def from(before:Int):String	=
			text substring (before, offset)

	private def consume() {
		if (finished)	sys error "already finished"
		offset	+= 1
	}
}
