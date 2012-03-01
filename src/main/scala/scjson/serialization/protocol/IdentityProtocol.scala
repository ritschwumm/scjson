package scjson.serialization

import scjson._

import JSONSerializationUtil._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of JSONValue as JSONValue */
trait IdentityProtocol {
	/*
	implicit val JSONValueJSONFormat:JSONFormat[JSONValue]	= 
			JSONFormat(identity, identity)
	*/
		
	implicit def JSONValueJSONFormat[T<:JSONValue]:JSONFormat[T]	= new JSONFormat[T] {
		def write(out:T):JSONValue	= out
		def read(in:JSONValue):T	= downcast(in)
	}	
}
