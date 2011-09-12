package scjson.serialization

import scjson._

object Operations {
	def format[T:Format] = implicitly[Format[T]]
	def doWrite[T:Format](out:T):JSValue	= format[T] write	out
	def doRead[T:Format](in:JSValue):T		= format[T] read	in
}
