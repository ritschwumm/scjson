package scjson.io

import java.io.*

import scjson.codec.*
import scjson.converter.*

enum JsonLoadFailure {
	case IoException(cause:IOException)
	case DecodeFailure(base:JsonDecodeFailure)
	case ParseFailure(base:JsonError)
}
