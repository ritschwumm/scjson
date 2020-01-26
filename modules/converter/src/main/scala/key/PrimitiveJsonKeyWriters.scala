package scjson.converter

import scutil.lang._

import scjson.converter.{
	NumberStringConverters	=> NS
}

trait PrimitiveJsonKeyWriters {
	implicit val StringKeyWriter:JsonKeyWriter[String]	= Converter total JsonKey.apply
	implicit val IntKeyWriter:JsonKeyWriter[Int]		= NS.IntToString	>=> StringKeyWriter
	implicit val LongKeyWriter:JsonKeyWriter[Long]		= NS.LongToString	>=> StringKeyWriter
	implicit val BigIntKeyWriter:JsonKeyWriter[BigInt]	= NS.BigIntToString	>=> StringKeyWriter
	implicit val UnitKeyWriter:JsonKeyWriter[Unit]		= NS.UnitToString	>=> StringKeyWriter
}
