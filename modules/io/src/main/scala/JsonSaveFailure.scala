package scjson.io

import java.io.*

import scjson.converter.*

enum JsonSaveFailure {
	case IoException(cause:IOException)
	case UnparseFailure(base:JsonError)
}
