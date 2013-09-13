package scjson.serialization

import scjson.JSONInputException

final class JSONDeserializationException(
	message:String, cause:Exception = null
)
extends JSONInputException(
	message, cause
)
