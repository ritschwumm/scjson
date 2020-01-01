package scjson.pickle.protocol

import scjson.ast._
import scjson.pickle._

import JsonPickleUtil._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of JsonValue as JsonValue */
trait IdentityProtocol {
	implicit def PassThroughFormat[T<:JsonValue]:Format[T]	=
		Format[T](
			identity,
			downcast[T]
		)
}
