package scjson.codec

import scala.collection.immutable

import scutil.lang._

import scjson._

private object JSONDecoder {
	/** parse a JSON formatted String into a JSONValue */
	def decode(s:String):Tried[JSONDecodeException,JSONValue]	=
			try { 
				Win(new JSONDecoder(s).decode()) 
			}
			catch { case e:JSONDecodeException => 
				Fail(e) 
			}
}

private final class JSONDecoder(text:String) {
	val NO_CHAR	= -1
	var	offset	= 0

	private def decode():JSONValue	= {
		val value	= decodeNext()
		ws()
		if (!finished)	throw expected("end of input")
		value
	}

	private def decodeNext():JSONValue = {
		ws()
		if (finished)		throw expected("any char")
			
		if (is("null"))	return JSONNull
		if (is("true"))	return JSONTrue
		if (is("false"))	return JSONFalse
		if (is('[')) {
			val	out	= new immutable.VectorBuilder[JSONValue] 
			ws()
			if (is(']'))	return JSONArray(out.result)
			while (true) {
				val value	= decodeNext()
				out	+= value
				ws()
				if (is(']'))	return JSONArray(out.result)
				if (!is(','))	throw expectedClass(",]")
			}
		}
		if (is('{')) {
			val out	= new immutable.VectorBuilder[(String,JSONValue)] 
			ws()
			if (is('}'))	return JSONObject(out.result)
			while (true) {
				val key	= decodeNext() match {
					case JSONString(s)	=> s
					case _				=> throw expected("string key")
				}
				ws();
				if (!is(':'))	throw expectedClass(":")
				val value	= decodeNext()
				out	+= (key -> value)
				ws()
				if (is('}'))	return JSONObject(out.result)
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
					return JSONString(out.result)
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
		
		val before	= offset
		
		var numCommit	= false
		numCommit	|= is('-')
	
		val beforeBody	= offset
		val numHead	= digits()
		if (numHead > 1 && (text charAt beforeBody) == '0') {
			offset	= beforeBody
			throw expected("number without leading zero")
		}
		val numDot	= is('.')
		val numTail	= digits()
		val numBody	= numHead != 0 || numTail != 0
		if (numCommit && !numBody) {
			offset	= beforeBody
			throw expected("valid number")
		}
		numCommit	= numBody
		
		if (numCommit) {
			if (is('e') || is('E')) {
				is('+') || is('-')
				val countExpo	= digits()
				if (countExpo == 0) {
					throw expected("at least 1 digit in the exponent")
				}
			}
			try {
				return JSONNumber(BigDecimal(from(before)))
			}
			catch {
				case e:NumberFormatException	=>
					offset	= before
					throw expected("valid number")
			}
		}
		else {
			offset	= before
			throw expected("unexpected character %04x" format (text charAt offset).toInt)
		}
	}
	
	private def expected(what:String)	=
			new JSONDecodeException(text, offset, what)
	
	private def expectedClass(charClass:String)	=
			new JSONDecodeException(text, offset, "one of " + (JSONCodec encode JSONString(charClass)))

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
	
	private def previous:Int	=
			if (offset == 0)	NO_CHAR
			else				text charAt offset-1
			
	private def from(before:Int):String	=
			text substring (before, offset)
	
	private def consume() {
		if (finished)	sys error "already finished"
		offset	+= 1
	}
}
