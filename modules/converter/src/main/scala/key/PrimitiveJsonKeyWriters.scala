package scjson.converter

import scutil.lang.*

import scjson.converter.{
	NumberStringConverters	as NS
}

trait PrimitiveJsonKeyWriters {
	given StringKeyWriter:JsonKeyWriter[String]	= Converter total JsonKey.apply
	given IntKeyWriter:JsonKeyWriter[Int]		= NS.IntToString	>=> StringKeyWriter
	given LongKeyWriter:JsonKeyWriter[Long]		= NS.LongToString	>=> StringKeyWriter
	given BigIntKeyWriter:JsonKeyWriter[BigInt]	= NS.BigIntToString	>=> StringKeyWriter
	given UnitKeyWriter:JsonKeyWriter[Unit]		= NS.UnitToString	>=> StringKeyWriter
}
