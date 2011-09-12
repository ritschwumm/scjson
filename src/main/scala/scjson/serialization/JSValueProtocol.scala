package scjson.serialization

import scjson._

object JSValueProtocol extends JSValueProtocol

trait JSValueProtocol {
	implicit def JSValueFormat[T<:JSValue]:Format[T]	= new Format[T] {
		def write(out:T):JSValue	= out
		def read(in:JSValue):T		= in.asInstanceOf[T]
	}
}
