package scjson.pickle.protocol

import scjson.ast._
import scjson.pickle._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of JsonValue as JsonValue */
trait IdentityProtocol {
	implicit def PassThroughFormat:Format[JsonValue]	=
		Format[JsonValue](
			identity,
			identity
		)
}
