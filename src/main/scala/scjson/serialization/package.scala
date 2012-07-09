package scjson

import scutil.data.Bijection

package object serialization {
	import JSONSerializationUtil._
	
	type JSONFormat[T] = Bijection[T,JSONValue]
	
	def JSONFormat[T](writeFunc:T=>JSONValue, readFunc:JSONValue=>T):JSONFormat[T]	= 
			Bijection(writeFunc, readFunc)
		
	// TODO ugly hack
	def JSONFormatSubtype[T,U<:JSONValue](writeFunc:T=>U, readFunc:U=>T):JSONFormat[T]	=
			Bijection(writeFunc, it => readFunc(downcast(it)))
		
	def JSONFormatLazy[T](sub: =>JSONFormat[T]):JSONFormat[T]	= new Bijection[T,JSONValue] {
		lazy val delegate = sub
		def write(t:T):JSONValue	= delegate write t
		def read(s:JSONValue):T		= delegate read s
	}
	
	//------------------------------------------------------------------------------
	
	def jsonFormat[T:JSONFormat]	= implicitly[JSONFormat[T]]
	
	def doWrite[T:JSONFormat](out:T):JSONValue	= jsonFormat[T] write	out
	def doRead[T:JSONFormat](in:JSONValue):T	= jsonFormat[T] read	in
}
