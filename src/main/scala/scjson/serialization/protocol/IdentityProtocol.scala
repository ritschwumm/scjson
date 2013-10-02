package scjson.serialization

import scjson._

import JSONSerializationUtil._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of JSONValue as JSONValue */
trait IdentityProtocol {
	implicit def JSONValueFormat[T<:JSONValue]:Format[T]	=
			Format[T](
				identity,
				downcast[T]
			)
}
