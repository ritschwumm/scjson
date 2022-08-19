package scjson.io

import java.io.*
import java.nio.file.Path

import scutil.core.implicits.*
import scutil.jdk.implicits.*
import scutil.lang.*

import scjson.ast.*
import scjson.codec.*
import scjson.converter.*

object JsonIo {
	def loadFilePath[T:JsonReader](path:Path):Either[JsonLoadFailure,T]	=
		loadFile(path.toFile)

	def saveFilePath[T:JsonWriter](path:Path, value:T, pretty:Boolean):Either[JsonSaveFailure,Unit]	=
		saveFile(path.toFile, value, pretty)

	// TODO path get rid of this
	def loadFile[T:JsonReader](file:File):Either[JsonLoadFailure,T]	=
		for {
			str	<- readFileString(file)	leftMap (it => JsonLoadFailure.IoException(it)	:JsonLoadFailure)
			ast	<- JsonCodec decode str	leftMap (it => JsonLoadFailure.DecodeFailure(it):JsonLoadFailure)
			out	<- readAst[T](ast)		leftMap (it => JsonLoadFailure.ParseFailure(it)	:JsonLoadFailure)
		}
		yield out

	// TODO path get rid of this
	def saveFile[T:JsonWriter](file:File, value:T, pretty:Boolean):Either[JsonSaveFailure,Unit]	=
		for {
			string	<-	writeString[T](value, pretty) leftMap {
							case JsonWriteFailure.UnparseFailure(x)	=> JsonSaveFailure.UnparseFailure(x):JsonSaveFailure
						}
			_		<-	writeFileString(file)(string)	leftMap JsonSaveFailure.IoException.apply
		}
		yield ()

	//------------------------------------------------------------------------------

	val charset	= Charsets.utf_8

	def readFilePathString(path:Path):Either[IOException,String]	=
		readFileString(path.toFile)

	def writeFilePathString(path:Path)(content:String):Either[IOException,Unit]	=
		writeFileString(path.toFile)(content)

	// TODO path get rid of this
	def readFileString(file:File):Either[IOException,String]	=
		try {
			Right(file readString charset)
		}
		catch { case e:IOException =>
			Left(e)
		}

	// TODO path get rid of this
	def writeFileString(file:File)(content:String):Either[IOException,Unit]	=
		try {
			file.writeString(charset, content)
			Right(())
		}
		catch { case e:IOException =>
			Left(e)
		}

	//------------------------------------------------------------------------------

	def readString[T:JsonReader](json:String):Either[JsonReadFailure,T]	=
		for {
			ast	<- JsonCodec decode json	leftMap	(it => JsonReadFailure.DecodeFailure(it):JsonReadFailure)
			out	<- readAst[T](ast)			leftMap (it => JsonReadFailure.ParseFailure(it)	:JsonReadFailure)
		}
		yield out

	def writeString[T:JsonWriter](value:T, pretty:Boolean):Either[JsonWriteFailure, String]	=
		for {
			ast	<-	writeAst(value)	leftMap (it => JsonWriteFailure.UnparseFailure(it):JsonWriteFailure)
		}
		yield JsonCodec.encode(ast, pretty)

	//------------------------------------------------------------------------------

	def readAst[T:JsonReader](json:JsonValue):Either[JsonError,T]	=
		JsonReader[T].convert(json).toEither

	def writeAst[T:JsonWriter](value:T):Either[JsonError,JsonValue]	=
		JsonWriter[T].convert(value).toEither
}
