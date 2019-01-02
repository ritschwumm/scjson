package scjson.converter

object JsonKeyWriters extends JsonKeyWriters

trait JsonKeyWriters
		extends	PrimitiveJsonKeyWriters
		with	NewtypeJsonKeyWriters
		with	EnumJsonKeyWriters
