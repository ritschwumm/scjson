package scjson.pickle.protocol

import scjson.ast._
import scjson.pickle._

import JSONPickleUtil._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of JSONValue as JSONValue */
trait IdentityProtocol {
	implicit def PassThroughFormat[T<:JSONValue]:Format[T]	=
			Format[T](
				identity,
				downcast[T]
			)
}
