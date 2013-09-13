package scjson

import java.io._

import scutil.lang._
import scutil.Implicits._
import scutil.io.Charsets

import scjson.codec._
import scjson.serialization._

object JSONIO {
	val charset	= Charsets.utf_8
	
	def loadFile[T:Format](file:File):Tried[JSONInputException,T]	=
			for {
				str	<- 
						try {
							Win(file readString charset)
						}
						catch { case e:Exception =>
							Fail(new JSONFileException(file, e))
						}
				out	<- readString(str)
			}
			yield out
							
	def saveFile[T:Format](file:File, value:T):Unit	=
			value			|>
			writeString[T]	|>
			{ file writeString (charset, _) }
			
	//------------------------------------------------------------------------------
	
	def readString[T:Format](json:String):Tried[JSONInputException,T]	=
			for {
				ast	<- JSONCodec decode json
				out	<- readAST(ast)
			}
			yield out
			
	def writeString[T:Format](value:T):String	=
			value		|>
			doWrite[T]	|>
			JSONCodec.encode
			
	//------------------------------------------------------------------------------
	
	def readAST[T:Format](json:JSONValue):Tried[JSONInputException,T]	=
			try {
				Win(doRead[T](json))
			}
			catch { 
				case e:JSONDeserializationException	=> Fail(e)
				case e:Exception					=> Fail(new JSONDeserializationException("cannot decode json", e))
			}
		
	def writeAST[T:Format](value:T):JSONValue	= 
			doWrite[T](value)
}
