package scjson

import scutil.lang._

import scjson.ast._

import scjson.pickle.JSONPickleUtil._

/** typeclass-based, bidirectional JSON serialization */
package object pickle {
	/** convert values to JSON and back */
	type Format[T] = Bijection[T,JSONValue]
	
	/** create a Format from the two halves of a Bijection */
	def Format[T](writeFunc:T=>JSONValue, readFunc:JSONValue=>T):Format[T]	=
			Bijection(writeFunc, readFunc)
		
	/** this is a bit of a hack to force a specific constructor to be used for decoding */
	def SubtypeFormat[T,U<:JSONValue](writeFunc:T=>U, readFunc:U=>T):Format[T]	=
			Bijection(writeFunc, it => readFunc(downcast(it)))
		
	/** delay the construction of an actual Format until it's used */
	def LazyFormat[T](sub: =>Format[T]):Format[T]	=
			Format(it => sub write it, it => sub read it)
	
	//------------------------------------------------------------------------------
	
	/** provide a Format for a specific value type */
	def format[T:Format]	= implicitly[Format[T]]
	
	/** encode a value into its JSON representation using an implicitly provided Format */
	def doWrite[T:Format](out:T):JSONValue	= format[T] write	out
	/** decode a value from its JSON representation using an implicitly provided Format */
	def doReadUnsafe[T:Format](in:JSONValue):T	= format[T] read	in
	
	/** decode a value from its JSON representation using an implicitly provided Format */
	def doRead[T:Format](in:JSONValue):Tried[JSONUnpickleFailure,T]	=
			try {
				Win(doReadUnsafe[T](in))
			}
			catch { case e:JSONUnpickleException =>
				Fail(e.failure)
			}
}
