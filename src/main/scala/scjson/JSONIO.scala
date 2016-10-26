package scjson

import java.io._

import scutil.lang._
import scutil.implicits._
import scutil.codec.Charsets

import scjson.codec._
import scjson.serialization._

object JSONIO {
	def loadFile[T:Format](file:File):Tried[JSONInputException,T]	=
			file			into
			readFileString	flatMap
			readString[T]
							
	def saveFile[T:Format](file:File, value:T):Unit	=
			value			|>
			writeString[T]	|>
			writeFileString(file)
			
	//------------------------------------------------------------------------------
			
	val charset	= Charsets.utf_8
	
	def readFileString(file:File):Tried[JSONInputException,String]	=
			try {
				Win(file readString charset)
			}
			catch { case e:Exception =>
				Fail(new JSONFileException(file, e))
			}
			
	def writeFileString(file:File)(content:String):Unit	=
			file writeString (charset, content)
			
	//------------------------------------------------------------------------------
	
	def readString[T:Format](json:String):Tried[JSONInputException,T]	=
			json				into
			JSONCodec.decode	flatMap
			readAST[T]
			
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
