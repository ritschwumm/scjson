package scjson.converter

import scutil.lang._
import scjson.ast._

trait JsonIdentity {
	given IdentityRW:JsonConverter[JsonValue,JsonValue]	=
		Converter.identity
}
