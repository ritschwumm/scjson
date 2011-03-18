package scjson

import scala.util.parsing.input._
import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._
import scala.util.parsing.combinator.lexical._

import scutil.Marshaller
	
object JSParser extends Marshaller[JSValue,String] {
	def apply(v:JSValue):String				= unparse(v)
	def unapply(s:String):Option[JSValue]	= parse(s)
			
	//------------------------------------------------------------------------------
	
	/** parse a JSON formatted String into a JSValue */
	def parse(s:String):Option[JSValue]	= TheParser parse s
	
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
		
		def value:Parser[JSValue]			= obj | arr | str | num | tru | fls | nul
		def arr:Parser[JSArray]				= "[" ~> repsep(value, ",") <~ "]"	^^ { x => JSArray(x) }
		def obj:Parser[JSObject]			= "{" ~> repsep(pair, ",") <~ "}"	^^ { x => JSObject(Map() ++ x) }
		def pair:Parser[(JSString,JSValue)]	= str ~ (":" ~> value)				^^ { case x ~ y => (x, y) }
		def str:Parser[JSString]			= accept("string", { case lexical.StringLit(x)	=> JSString(x) })
		def num:Parser[JSNumber]			= accept("number", { case lexical.NumericLit(x)	=> JSNumber(BigDecimal(x)) })
		def tru:Parser[JSBoolean]			= "true"  ^^^ JSTrue
		def fls:Parser[JSBoolean]			= "false" ^^^ JSFalse
		def nul:Parser[JSValue]				= "null"  ^^^ JSNull
	}
	
	//------------------------------------------------------------------------------
	
	/** unparse a JSValue into a String */ 
	def unparse(v:JSValue):String	= v match {
		case JSNull			=> "null"
		case JSTrue			=> "true"
		case JSFalse		=> "false"
		case JSNumber(data)	=> data.toString
		case JSString(data)	=> data map unparseChar mkString("\"","","\"") 
		case JSArray(data)	=> data map unparse mkString("[", ",", "]")
		case JSObject(data)	=> data.iterator map { case (key,value)	=> unparse(key) + ":" + unparse(value) } mkString("{", ",", "}")
	}
	
	private def unparseChar(char:Char):String	= char match {
		case '"' 	=> "\\\""
		case '\\'	=>	"\\\\"
		// this would be allowed but is ugly
		//case '/'	=> "\\/"
		// these are optional
		case '\b'	=> "\\b"
		case '\f'	=> "\\f"
		case '\n'	=> "\\n"
		case '\r'	=> "\\r"
		case '\t'	=> "\\t"
		case c 
		if c < 32	=> "\\u%04x".format(c.toInt)
		case c 		=> c.toString
	}
}
