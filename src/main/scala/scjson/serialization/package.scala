package scjson

import scutil.lang._

/** typeclass-bases, bidirectional JSON serialization */
package object serialization {
	import JSONSerializationUtil._
	
	/** convert values to JSON and back */
	type Format[T] = Bijection[T,JSONValue]
	
	/** create a Format from the two halves of a Bijection */
	def Format[T](writeFunc:T=>JSONValue, readFunc:JSONValue=>T):Format[T]	= 
			Bijection(writeFunc, readFunc)
		
	/** this is a bit of a hack to force a specific constructor to be used for decoding */
	def SubtypeFormat[T,U<:JSONValue](writeFunc:T=>U, readFunc:U=>T):Format[T]	=
			Bijection(writeFunc, it => readFunc(downcast(it)))
		
	/** delay the construction of an actual Format until it's used */
	def LazyFormat[T](sub: =>Format[T]):Format[T]	= new Bijection[T,JSONValue] {
		lazy val delegate = sub
		def write(t:T):JSONValue	= delegate write t
		def read(s:JSONValue):T		= delegate read s
	}
	
	//------------------------------------------------------------------------------
	
	/** provide a Format for a specific value type */
	def format[T:Format]	= implicitly[Format[T]]
	
	/** encode a value into its JSON representation using an implicitly provided Format */
	def doWrite[T:Format](out:T):JSONValue	= format[T] write	out
	/** decode a value from its JSON representation using an implicitly provided Format */
	def doRead[T:Format](in:JSONValue):T	= format[T] read	in
}
