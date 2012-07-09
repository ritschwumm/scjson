package scjson.serialization

import scjson._

private object Summand {
	val typeTag	= ""
	
	implicit def fromClass[T:JSONFormat](clazz:Class[T]):Summand[T]	= 
			Summand(clazz.getName, clazz, jsonFormat[T])
	implicit def fromJSONFormat[T:Manifest](format:JSONFormat[T]):Summand[T]	= 
			Summand(manifest[T].erasure.getName, manifest[T].erasure, format)
		
	implicit def fromClassWithIdentifier[T:JSONFormat](id:(String,Class[T])):Summand[T]	= 
			Summand(id._1, id._2, jsonFormat[T])
	implicit def fromJSONFormatWithIdentifier[T:Manifest](id:(String, JSONFormat[T])):Summand[T]	=
			Summand(id._1, manifest[T].erasure, id._2)
}

private case class Summand[T](identifier:String, clazz:Class[_], format:JSONFormat[T]) {
	def this(clazz:Class[_], format:JSONFormat[T])	= this(clazz.getName, clazz, format)
}
