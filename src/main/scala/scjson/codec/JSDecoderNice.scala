package scjson.codec

import scala.util.parsing.input._
import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._
import scala.util.parsing.combinator.lexical._

import scjson._

object JSDecoderNice {
	/** parse a JSON formatted String into a JSValue */
	def read(s:String):Option[JSValue]	= TheParser parse s
	
	private object TheParser extends StdTokenParsers with ImplicitConversions {
		/** parse a JSON formatted String into a JSValue */
		def parse(s:String):Option[JSValue]	= parse(new CharSequenceReader(s))
		
		def parse(input:Reader[Char]):Option[JSValue] = phrase(value)(new lexical.Scanner(input)) match {
			case Success(result, _)	=> Some(result)
			case _					=> None
		}
		
		//------------------------------------------------------------------------------
		
		type Tokens	= scala.util.parsing.json.Lexer
		
		val lexical	= new Tokens
		lexical.reserved	++= List("true", "false", "null")
		lexical.delimiters	++= List("{", "}", "[", "]", ":", ",")
		
		lazy val value:Parser[JSValue]				= obj | arr | str | num | tru | fls | nul
		lazy val arr:Parser[JSArray]				= "[" ~> repsep(value, ",") <~ "]"	^^ { x => JSArray(x) }
		lazy val obj:Parser[JSObject]				= "{" ~> repsep(pair, ",") <~ "}"	^^ { x => JSObject(Map() ++ x) }
		lazy val pair:Parser[(JSString,JSValue)]	= str ~ (":" ~> value)				^^ { case x ~ y => (x, y) }
		lazy val str:Parser[JSString]				= accept("string", { case lexical.StringLit(x)	=> JSString(x) })
		lazy val num:Parser[JSNumber]				= accept("number", { case lexical.NumericLit(x)	=> JSNumber(BigDecimal(x)) })
		lazy val tru:Parser[JSBoolean]				= "true"  ^^^ JSTrue
		lazy val fls:Parser[JSBoolean]				= "false" ^^^ JSFalse
		lazy val nul:Parser[JSValue]				= "null"  ^^^ JSNull
	}
}
