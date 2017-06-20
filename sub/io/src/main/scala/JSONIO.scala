package scjson.io

import java.io._

import scutil.base.implicits._
import scutil.core.implicits._
import scutil.lang._

import scjson.ast._
import scjson.codec._
import scjson.pickle._

object JSONIO {
	def loadFile[T:Format](file:File):Either[JSONIOFileFailure,T]	=
			for {
				str	<- readFileString(file)	mapLeft JSONIOExceptionFailure.apply
				ast	<- JSONCodec decode str	mapLeft JSONIODecodeFailure.apply
				out	<- readAST[T](ast)		mapLeft JSONIOUnpickleFailure.apply
			}
			yield out
			
	// TODO return a custom failure
	def saveFile1[T:Format](file:File, value:T, pretty:Boolean):Option[IOException]	=
			writeString[T](value, pretty)	|>
			writeFileString1(file)
			
	//------------------------------------------------------------------------------
			
	val charset	= Charsets.utf_8
	
	def readFileString(file:File):Either[IOException,String]	=
			try {
				Right(file readString charset)
			}
			catch { case e:IOException =>
				Left(e)
			}
			
	def writeFileString1(file:File)(content:String):Option[IOException]	=
			try {
				file writeString (charset, content)
				None
			}
			catch { case e:IOException =>
				Some(e)
			}
			
	//------------------------------------------------------------------------------
	
	def readString[T:Format](json:String):Either[JSONIOStringFailure,T]	=
			for {
				ast	<- JSONCodec decode json	mapLeft JSONIODecodeFailure.apply
				out	<- readAST[T](ast)			mapLeft JSONIOUnpickleFailure.apply
			}
			yield out
			
	def writeString[T:Format](value:T, pretty:Boolean):String	=
			doWrite[T](value)	|>
			(JSONCodec encode (_, pretty))
			
	//------------------------------------------------------------------------------
	
	def readAST[T:Format](json:JSONValue):Either[JSONUnpickleFailure,T]	=
			doRead[T](json)
		
	def writeAST[T:Format](value:T):JSONValue	=
			doWrite[T](value)
}
