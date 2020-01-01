package scjson.converter

import scutil.lang._
import scjson.ast._

trait JsonIdentity {
	implicit val IdentityRW:JsonConverter[JsonValue,JsonValue]	=
		Converter.identity
}
