package scjson.codec

import scala.collection.immutable

import scjson.ast._

// BETTER unused should be private
protected object JsonDecoder {
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
		if (finished)		throw expected("any char")
			
		if (is("null"))		return JsonNull
		if (is("true"))		return JsonTrue
		if (is("false"))	return JsonFalse
		if (is('[')) {
			val	out	= new immutable.VectorBuilder[JsonValue]
			ws()
			if (is(']'))	return JsonArray(out.result)
			while (true) {
				val value	= decodeNext()
				out	+= value
				ws()
				if (is(']'))	return JsonArray(out.result)
				if (!is(','))	throw expectedClass(",]")
			}
		}
		if (is('{')) {
			val out	= new immutable.VectorBuilder[(String,JsonValue)]
			ws()
			if (is('}'))	return JsonObject(out.result)
			while (true) {
				val key	= decodeNext() match {
					case JsonString(s)	=> s
					case _				=> throw expected("string key")
				}
				ws();
				if (!is(':'))	throw expectedClass(":")
				val value	= decodeNext()
				out	+= (key -> value)
				ws()
				if (is('}'))	return JsonObject(out.result)
				if (!is(','))	throw expectedClass(",}");
			}
		}
		if (is('"')) {
			val out	= new StringBuilder
			while (true) {
				if (is('\\')) {
					if (finished)	throw expected("escape continuation")
						 if (is('"'))	out	+= '"'
					else if (is('\\'))	out	+= '\\'
					else if (is('/'))	out	+= '/'
					else if (is('t'))	out	+= '\t'
					else if (is('r'))	out	+= '\r'
					else if (is('n'))	out	+= '\n'
					else if (is('f'))	out	+= '\f'
					else if (is('b'))	out	+= '\b'
					else if (is('u')) {
						if (offset+4 > text.length)	throw expected("4 hex digits")
						
						val h1	= hexDigit()
						val h2	= hexDigit()
						val h3	= hexDigit()
						val h4	= hexDigit()
						
						out	+= ((h1 << 12) | (h2 << 8) | (h3 << 4) | (h4 << 0)).toChar
					}
					else throw expectedClass("\"\\/trnfbu")
				}
				else if (is('"')) {
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
		
		val numNeg		= is('-')
		val beforeBody	= offset
		val numInt		= digits()
		if (numNeg || numInt != 0) {
			if (numInt == 0) {
				// TODO [.eE+-n] could issue special errors
				// offset	= beforeBody
				throw expected("number digits")
			}
			if (numInt > 1 && (text charAt beforeBody) == '0') {
				offset	= beforeBody
				throw expected("number digits without leading zero")
			}
			if (is('.')) {
				val numTail	= digits()
				if (numTail == 0)	throw expected("fraction digits after dot")
			}
			if (is('e') || is('E')) {
				is('+') || is('-')
				val countExpo	= digits()
				if (countExpo == 0)	throw expected("at least one digit in the exponent")
			}
			try {
				return JsonNumber(BigDecimal(from(before)))
			}
			catch {
				case e:NumberFormatException	=>
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
	
	private def is(c:Char):Boolean	= {
			 if (finished)	false
		else if (next != c)	false
		else				{ consume(); true }
	}
	
	private def is(s:String):Boolean	= {
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
