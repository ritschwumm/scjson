package scjson.converter

object JsonKeyReaders extends JsonKeyReaders

trait JsonKeyReaders
		extends	PrimitiveJsonKeyReaders
		with	NewtypeJsonKeyReaders
		with	EnumJsonKeyReaders
