package scjson.converter

import scutil.lang.*

trait PrimitiveJsonKeyWriters {
	given StringKeyWriter:JsonKeyWriter[String]	= Converter.total(JsonKey.apply)
	given IntKeyWriter:JsonKeyWriter[Int]		= NumberStringConverters.IntToString	>=> StringKeyWriter
	given LongKeyWriter:JsonKeyWriter[Long]		= NumberStringConverters.LongToString	>=> StringKeyWriter
	given BigIntKeyWriter:JsonKeyWriter[BigInt]	= NumberStringConverters.BigIntToString	>=> StringKeyWriter
	given UnitKeyWriter:JsonKeyWriter[Unit]		= NumberStringConverters.UnitToString	>=> StringKeyWriter
}
