package scjson.codec

import scala.util.control.Exception._
import scala.collection.mutable

import scjson._

object JSONDecoderFast {
	/** parse a JSON formatted String into a JSONValue */
	def read(s:String):Either[JSONDecodeException,JSONValue]	=
			try { Right(readOrThrow(s)) }
			catch { case e:JSONDecodeException => Left(e) }
			
	/** parse a JSON formatted String into a JSONValue */
	def readOrThrow(s:String):JSONValue	= 
			new JSONDecoderFast(s).decode()
}

private final class JSONDecoderFast(text:String) {
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
		if (is("null"))		return JSONNull
		if (is("true"))		return JSONTrue
		if (is("false"))	return JSONFalse
		if (is('[')) {
			val	out	= mutable.ArrayBuilder.make[JSONValue] 
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
			val out	= new mutable.MapBuilder[JSONString,JSONValue,Map[JSONString,JSONValue]](Map.empty)
			ws()
			if (is('}'))	return JSONObject(out.result)
			while (true) {
				val key	= decodeNext() match {
					case s:JSONString	=> s
					case _			=> throw expected("string key")
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
			val out	= new mutable.StringBuilder
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
						// TODO slow
						if (offset+4 > text.length)	throw expected("4 hex digits")
						val before	= offset
						offset += 4
						try {
							out	+= Integer.parseInt(from(before), 16).toChar
						}
						catch {
							case e:NumberFormatException	=>
								offset -= 4
								throw expected("4 hex digits")
						}
					}
					/*
					// NOTE broken
					else if (is('u')) {
						val	h	= hex4()
						if (h == NO_CHAR)	throw expected("4 hex digits")
						out	+= h.toChar
					}
					*/
					else throw expectedClass("\"\\/trnfbu")
				}
				else if (is('"')) {
					return JSONString(out.result)
				}
				else if (rng('\u0000', '\u001f')) {
					offset	-= 1
					throw expected("no control character")
				}
				else {
					if (finished)	throw expected("more chars")
					out	+= next.toChar
					consume()
				}
			}
		}
		val before	= offset
		is('-')
		if (next == '0') {
			consume()
			if (next >= '0' && next <= '9')	throw expected("number without leading zero")
		}
		digits()
		is('.')
		digits()
		is('e') || is('E')
		is('+') || is('-')
		digits()
		try {
			JSONNumber(BigDecimal(from(before)))
		}
		catch {
			case e:NumberFormatException	=>
				offset	= before
				throw expected("valid number")
		}
	}
	
	private def expected(what:String)	=
			new JSONDecodeException(text, offset, what)
	
	private def expectedClass(charClass:String)	=
			// TODO JSON-encode charClass characters
			new JSONDecodeException(text, offset, "one of " + charClass)

	//------------------------------------------------------------------------------
	//## hex
	
	/*
	private def hex4():Int	= {
		val before	= offset
		val	h1	= hexDigit;	if (h1 != NO_CHAR)	{ offset += 1 } else { offset = before; return NO_CHAR }
		val	h2	= hexDigit;	if (h2 != NO_CHAR)	{ offset += 1 } else { offset = before; return NO_CHAR }
		val	h3	= hexDigit;	if (h3 != NO_CHAR)	{ offset += 1 } else { offset = before; return NO_CHAR }
		val	h4	= hexDigit;	if (h4 != NO_CHAR)	{ offset += 1 } else { offset = before; return NO_CHAR }
		(h1 << 12) | (h2 << 8) | (h3 << 4) | (h4 << 1) 
	}
			
	private def hexDigit:Int	= {
		val	c	= next
			 if (c >= '0' && c <= '9')	c - '0'
		else if (c >= 'a' && c <= 'f')	c - 'a' + 10
		else							NO_CHAR
	}
	*/

	//-------------------------------------------------------------------------
	//## tokens
	
	private def digits():Boolean	= {
		val before	= offset
		var keepOn	= true
		while (!finished && keepOn) {
			val c	= next
			if (c >= '0' && c <= '9')	consume()
			else						keepOn	= false
		}
		offset != before
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
	
//	private boolean ws() {
//		int	before	= offset;
//		for (;;) {
//			if (is(' '))	continue;
//			if (is('\t'))	continue;
//			if (is('\r'))	continue;
//			if (is('\n'))	continue;
//			break;
//		}
//		return offset != before;
//	}
	
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
