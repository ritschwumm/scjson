package scjson.io

import scjson.converter.*

enum JsonWriteFailure {
	case UnparseFailure(base:JsonError)
}
