package scjson.serialization

import scjson._

import JSONSerializationUtil._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of JSONValue as JSONValue */
trait IdentityProtocol {
	/*
	implicit val JSONValueFormat:Format[JSONValue]	= 
			Format(identity, identity)
	*/
		
	implicit def JSONValueFormat[T<:JSONValue]:Format[T]	= new Format[T] {
		def write(out:T):JSONValue	= out
		def read(in:JSONValue):T	= downcast(in)
	}	
}
