package scjson

import scutil.lang._

import scjson.ast._

import scjson.pickle.JsonPickleUtil._

/** typeclass-based, bidirectional Json serialization */
package object pickle {
	/** convert values to Json and back */
	type Format[T] = Bijection[T,JsonValue]
	
	/** create a Format from the two halves of a Bijection */
	def Format[T](writeFunc:T=>JsonValue, readFunc:JsonValue=>T):Format[T]	=
			Bijection(writeFunc, readFunc)
		
	/** this is a bit of a hack to force a specific constructor to be used for decoding */
	def SubtypeFormat[T,U<:JsonValue](writeFunc:T=>U, readFunc:U=>T):Format[T]	=
			Bijection(writeFunc, it => readFunc(downcast(it)))
		
	/** delay the construction of an actual Format until it's used */
	def LazyFormat[T](sub: =>Format[T]):Format[T]	=
			Format(it => sub write it, it => sub read it)
	
	//------------------------------------------------------------------------------
	
	/** provide a Format for a specific value type */
	def format[T:Format]	= implicitly[Format[T]]
	
	/** encode a value into its Json representation using an implicitly provided Format */
	def doWrite[T:Format](out:T):JsonValue	= format[T] write	out
	/** decode a value from its Json representation using an implicitly provided Format */
	def doReadUnsafe[T:Format](in:JsonValue):T	= format[T] read	in
	
	/** decode a value from its Json representation using an implicitly provided Format */
	def doRead[T:Format](in:JsonValue):Either[JsonUnpickleFailure,T]	=
			try {
				Right(doReadUnsafe[T](in))
			}
			catch { case e:JsonUnpickleException =>
				Left(e.failure)
			}
}
