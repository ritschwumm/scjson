package scjson

import java.io._

import scutil.Implicits._
import scutil.Charsets

import scjson.serialization._

object JSONSerialization {
	private val charset	= Charsets.utf_8
	
	def loadFile[T:Format](file:File):T	=
			(file readString charset)	|>
			readString[T]
							
	def saveFile[T:Format](file:File, value:T):Unit	=
			value			|>
			writeString[T]	|>
			{ file writeString (charset, _) }
			
	def readString[T:Format](json:String):T	=
			json																		|>
			JSONMarshaller.read															|>
			{ _ getOrElse (throw new JSONDeserializationException("invalid JSON")) }	|>
			doRead[T]
			
	def writeString[T:Format](value:T):String	=
			value		|>
			doWrite[T]	|>
			JSONMarshaller.write
			
	def readJSON[T:Format](json:JSONValue):T	= doRead[T](json)
	def writeJSON[T:Format](value:T):JSONValue	= doWrite[T](value)
}
