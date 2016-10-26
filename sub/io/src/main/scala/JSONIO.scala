package scjson.io

import java.io._

import scutil.base.implicits._
import scutil.core.implicits._
import scutil.lang._

import scjson.ast._
import scjson.codec._
import scjson.pickle._

object JSONIO {
	def loadFile[T:Format](file:File):Tried[JSONIOFileFailure,T]	=
			for {
				str	<- readFileString(file)	mapFail JSONIOExceptionFailure.apply
				ast	<- JSONCodec decode str	mapFail JSONIODecodeFailure.apply
				out	<- readAST[T](ast)		mapFail JSONIOUnpickleFailure.apply
			}
			yield out
							
	def saveFile[T:Format](file:File, value:T):Unit	=
			value			|>
			writeString[T]	|>
			writeFileString(file)
			
	//------------------------------------------------------------------------------
			
	private val charset	= Charsets.utf_8
	
	private def readFileString(file:File):Tried[IOException,String]	=
			try {
				Win(file readString charset)
			}
			catch { case e:IOException =>
				Fail(e)
			}
			
	// TODO this throws exceptions, too
	private def writeFileString(file:File)(content:String):Unit	=
			file writeString (charset, content)
			
	//------------------------------------------------------------------------------
	
	def readString[T:Format](json:String):Tried[JSONIOStringFailure,T]	=
			for {
				ast	<- JSONCodec decode json	mapFail JSONIODecodeFailure.apply
				out	<- readAST[T](ast)			mapFail JSONIOUnpickleFailure.apply
			}
			yield out
			
	def writeString[T:Format](value:T):String	=
			value		|>
			doWrite[T]	|>
			JSONCodec.encode
			
	//------------------------------------------------------------------------------
	
	def readAST[T:Format](json:JSONValue):Tried[JSONUnpickleFailure,T]	=
			doRead[T](json)
		
	def writeAST[T:Format](value:T):JSONValue	=
			doWrite[T](value)
}
