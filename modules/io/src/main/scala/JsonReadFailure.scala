package scjson.io

import scjson.codec.*
import scjson.converter.*

enum JsonReadFailure {
	case DecodeFailure(base:JsonDecodeFailure)
	case ParseFailure(base:JsonError)
}
