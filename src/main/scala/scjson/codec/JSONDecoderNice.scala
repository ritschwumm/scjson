package scjson.codec

import scala.util.parsing.input._
import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._
import scala.util.parsing.combinator.lexical._

import scjson._

object JSONDecoderNice {
	/** parse a JSON formatted String into a JSONValue */
	def read(s:String):Option[JSONValue]	= TheParser parse s
	
	private object TheParser extends StdTokenParsers with ImplicitConversions {
		/** parse a JSON formatted String into a JSONValue */
		def parse(s:String):Option[JSONValue]	= parse(new CharSequenceReader(s))
		
		def parse(input:Reader[Char]):Option[JSONValue] = phrase(value)(new lexical.Scanner(input)) match {
			case Success(result, _)	=> Some(result)
			case _					=> None
		}
		
		//------------------------------------------------------------------------------
		
		type Tokens	= scala.util.parsing.json.Lexer
		
		val lexical	= new Tokens
		lexical.reserved	++= List("true", "false", "null")
		lexical.delimiters	++= List("{", "}", "[", "]", ":", ",")
		
		lazy val value:Parser[JSONValue]		= obj | arr | str | num | tru | fls | nul
		lazy val arr:Parser[JSONArray]			= "[" ~> repsep(value, ",") <~ "]"	^^ JSONArray.apply
		lazy val obj:Parser[JSONObject]			= "{" ~> repsep(elm, ",") <~ "}"	^^ JSONObject.apply
		lazy val elm:Parser[(String,JSONValue)]	= st1 ~ (":" ~> value)				^^ { case x ~ y => (x, y) }
		lazy val str:Parser[JSONString]			= st1 								^^ JSONString.apply
		lazy val st1:Parser[String]				= accept("string", { case lexical.StringLit(x)	=> x })
		lazy val num:Parser[JSONNumber]			= accept("number", { case lexical.NumericLit(x)	=> JSONNumber(BigDecimal(x)) })
		lazy val tru:Parser[JSONBoolean]		= "true"  ^^^ JSONTrue
		lazy val fls:Parser[JSONBoolean]		= "false" ^^^ JSONFalse
		lazy val nul:Parser[JSONValue]			= "null"  ^^^ JSONNull
	}
}