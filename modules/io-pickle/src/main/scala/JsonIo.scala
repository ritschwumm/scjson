package scjson.io.pickle

import java.io._

import scutil.base.implicits._
import scutil.core.implicits._
import scutil.lang._

import scjson.ast._
import scjson.codec._
import scjson.pickle._

object JsonIo {
	def loadFile[T:Format](file:File):Either[JsonLoadFailure,T]	=
		for {
			str	<- readFileString(file)	leftMap (it => JsonLoadFailure.IoException(it)		:JsonLoadFailure)
			ast	<- JsonCodec decode str	leftMap (it => JsonLoadFailure.DecodeFailure(it)	:JsonLoadFailure)
			out	<- readAst[T](ast)		leftMap (it => JsonLoadFailure.UnpickleFailure(it)	:JsonLoadFailure)
		}
		yield out

	def saveFile[T:Format](file:File, value:T, pretty:Boolean):Either[JsonSaveFailure,Unit]	=
		writeString[T](value, pretty)	|>
		(writeFileString(file)(_) leftMap JsonSaveFailure.IoException.apply _)

	//------------------------------------------------------------------------------

	val charset	= Charsets.utf_8

	def readFileString(file:File):Either[IOException,String]	=
		try {
			Right(file readString charset)
		}
		catch { case e:IOException =>
			Left(e)
		}

	def writeFileString(file:File)(content:String):Either[IOException,Unit]	=
		try {
			file writeString (charset, content)
			Right(())
		}
		catch { case e:IOException =>
			Left(e)
		}

	//------------------------------------------------------------------------------

	def readString[T:Format](json:String):Either[JsonReadFailure,T]	=
		for {
			ast	<- JsonCodec decode json	leftMap	(it => JsonReadFailure.DecodeFailure(it)	:JsonReadFailure)
			out	<- readAst[T](ast)			leftMap (it => JsonReadFailure.UnpickleFailure(it)	:JsonReadFailure)
		}
		yield out

	def writeString[T:Format](value:T, pretty:Boolean):String	=
		writeAst(value)	|>
		(JsonCodec encode (_, pretty))

	//------------------------------------------------------------------------------

	def readAst[T:Format](json:JsonValue):Either[JsonUnpickleFailure,T]	=
		doRead[T](json)

	def writeAst[T:Format](value:T):JsonValue	=
		doWrite[T](value)
}
