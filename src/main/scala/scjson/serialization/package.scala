package scjson

import scutil.lang._

/** typeclass-bases, bidirectional JSON serialization */
package object serialization {
	import JSONSerializationUtil._
	
	/** convert values to JSON and back */
	type JSONFormat[T] = Bijection[T,JSONValue]
	
	/** create a JSONFormat from the two halves of a Bijection */
	def JSONFormat[T](writeFunc:T=>JSONValue, readFunc:JSONValue=>T):JSONFormat[T]	= 
			Bijection(writeFunc, readFunc)
		
	/** this is a bit of a hack to force a specific constructor to be used for decoding */
	def JSONFormatSubtype[T,U<:JSONValue](writeFunc:T=>U, readFunc:U=>T):JSONFormat[T]	=
			Bijection(writeFunc, it => readFunc(downcast(it)))
		
	/** delay the construction of an actual JSONFormat until it's used */
	def JSONFormatLazy[T](sub: =>JSONFormat[T]):JSONFormat[T]	= new Bijection[T,JSONValue] {
		lazy val delegate = sub
		def write(t:T):JSONValue	= delegate write t
		def read(s:JSONValue):T		= delegate read s
	}
	
	//------------------------------------------------------------------------------
	
	/** provide a JSONFormat for a specific value type */
	def jsonFormat[T:JSONFormat]	= implicitly[JSONFormat[T]]
	
	/** encode a value into its JSON representation using an implicitly provided JSONFormat */
	def doWrite[T:JSONFormat](out:T):JSONValue	= jsonFormat[T] write	out
	/** decode a value from its JSON representation using an implicitly provided JSONFormat */
	def doRead[T:JSONFormat](in:JSONValue):T	= jsonFormat[T] read	in
}
