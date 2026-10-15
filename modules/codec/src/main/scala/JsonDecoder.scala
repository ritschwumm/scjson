package scjson.codec

import scala.collection.immutable

import scjson.ast.*

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
		if !finished	then throw expected("end of input")
		value
	}

	private def decodeNext():JsonValue = {
		ws()
		if finished		then throw expected("any char")

		if isChar('{') then {
			val out	= new immutable.VectorBuilder[(String,JsonValue)]
			ws()
			if isChar('}')	then return JsonValue.fromFields(out.result())
			while true do {
				// TODO decode only strings here, nothing else!
				val key	= decodeNext().asString getOrElse { throw expected("string key") }
				ws();
				if !isChar(':')	then throw expectedClass(":")
				val value	= decodeNext()
				out	+= (key -> value)
				ws()
				if isChar('}')	then return JsonValue.fromFields(out.result())
				if !isChar(',')	then expectedClass(",}");
			}
		}
		if isChar('[') then {
			val	out	= new immutable.VectorBuilder[JsonValue]
			ws()
			if isChar(']')	then return JsonValue.fromItems(out.result())
			while true do {
				val value	= decodeNext()
				out	+= value
				ws()
				if isChar(']')	then return JsonValue.fromItems(out.result())
				if !isChar(',')	then throw expectedClass(",]")
			}
		}
		if isChar('"') then {
			val out	= new StringBuilder
			while true do {
				if isChar('"')then {
					return JsonValue.fromString(out.result())
				}
				else if isChar('\\') then {
					if (finished)			then throw expected("escape continuation")

					if		isChar('"')		then out	+= '"'
					else if	isChar('\\')	then out	+= '\\'
					else if	isChar('/')		then out	+= '/'
					else if	isChar('t')		then out	+= '\t'
					else if	isChar('r')		then out	+= '\r'
					else if	isChar('n')		then out	+= '\n'
					else if	isChar('f')		then out	+= '\f'
					else if	isChar('b')		then out	+= '\b'
					else if	isChar('u')		then {
						if offset+4 > text.length	then throw expected("4 hex digits")

						val h1	= hexDigit()
						val h2	= hexDigit()
						val h3	= hexDigit()
						val h4	= hexDigit()

						out	+= ((h1 << 12) | (h2 << 8) | (h3 << 4) | (h4 << 0)).toChar
					}
					else throw expectedClass("\"\\/trnfbu")
				}
				else if rng('\u0000', '\u001f') then {
					offset	-= 1
					throw expected("not a control character")
				}
				else {
					if finished	then throw expected("more chars")
					out	+= next.toChar
					consume()
				}
			}
		}
		if isString("true")		then return JsonValue.True
		if isString("false")	then return JsonValue.False
		if isString("null")		then return JsonValue.Null

		val before		= offset

		val numNeg		= isChar('-')
		val beforeInt	= offset
		val numInt		= digits()
		if numNeg || numInt != 0 then {
			if numInt == 0 then {
				// TODO [.eE+-n] could issue special errors
				// offset	= beforeBody
				throw expected("number digits")
			}
			if numInt > 1 && text.charAt(beforeInt) == '0' then {
				offset	= beforeInt
				throw expected("number digits without leading zero")
			}
			if isChar('.') then {
				val numFrac	= digits()
				if numFrac == 0	then throw expected("fraction digits after dot")
			}
			if isChar('e') || isChar('E') then {
				/*
				val signExpo	=
						if		(isChar('+'))	true
						else if (isChar('-'))	false
						else					true
				*/
				isChar('+') || isChar('-')
				val countExpo	= digits()
				if countExpo == 0	then throw expected("at least one digit in the exponent")
			}
			try {
				return JsonValue.fromBigDecimal(BigDecimal(from(before)))
			}
			catch { case _:NumberFormatException	=>
				offset	= before
				throw expected("valid number")
			}
		}

		// no recognized token at all
		offset	= before
		throw expected("unexpected character %04x".format((text.charAt(offset)).toInt))
	}

	private def expected(what:String)	=
		new JsonDecodeException(JsonDecodeFailure(text, offset, what))

	private def expectedClass(charClass:String)	=
		new JsonDecodeException(JsonDecodeFailure(text, offset, "one of " + JsonCodec.encodeShort(JsonValue.fromString(charClass))))

	//-------------------------------------------------------------------------
	//## tokens

	private def hexDigit():Int	= {
		val	c	= text.charAt(offset)
		val h	=
			if		c >= '0' && c <= '9'	then c - '0'
			else if	c >= 'a' && c <= 'f'	then c - 'a' + 10
			else if	c >= 'A' && c <= 'F'	then c - 'A' + 10
			else throw expected("a hex digit")
		offset	+= 1
		h
	}

	private def digits():Int	= {
		val before	= offset
		var keepOn	= true
		while !finished && keepOn do {
			val c	= next
			if c >= '0' && c <= '9'
			then consume()
			else keepOn	= false
		}
		offset - before
	}

	private def ws():Unit	= {
		var keepOn	= true
		while !finished && keepOn do {
			val c	= next
			if (
				c == ' '	||
				c == '\t'	||
				c == '\r'	||
				c == '\n'
			)
			then consume()
			else keepOn	= false
		}
	}

	private def rng(start:Char, end:Char):Boolean	= {
		val c	= next
		if		c == NO_CHAR			then false
		else if	c < start || c > end	then false
		else							{ consume(); true }
	}

	private def isChar(c:Char):Boolean	= {
		if		finished	then false
		else if next != c	then false
		else				{ consume(); true }
	}

	/*
	private def isString(s:String):Boolean	= {
		val end	= offset + s.length
		if		(end > text.length)						false
		else if	((text.substring(offset, end)) != s)	false
		else											{ offset	= end; true }
	}
	*/

	private def isString(s:String):Boolean	= {
		val end	= offset + s.length
		if end <= text.length then {
			var i	= 0
			while i < s.length do {
				if text.charAt(offset+i) != s.charAt(i)	then return false
				i	+= 1
			}
			offset	= end
			true
		}
		else false
	}

	//-------------------------------------------------------------------------
	//## chars

	private def finished:Boolean	=
		offset == text.length

	private def next:Int	=
		if finished
		then NO_CHAR
		else text.charAt(offset)

	/*
	private def previous:Int	=
		if (offset == 0)	NO_CHAR
		else				text.charAt(offset-1)
	*/

	private def from(before:Int):String	=
		text.substring(before, offset)

	private def consume():Unit	= {
		if finished	then sys.error("already finished")
		offset	+= 1
	}
}
