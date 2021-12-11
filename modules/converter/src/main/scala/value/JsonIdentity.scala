package scjson.converter

import scutil.lang.*
import scjson.ast.*

trait JsonIdentity {
	given IdentityRW:JsonConverter[JsonValue,JsonValue]	=
		Converter.identity
}
