package scjson.converter

import scutil.lang.*

import scjson.converter.{
	NumberStringConverters	as NS
}

trait PrimitiveJsonKeyReaders {
	given StringKeyReader:JsonKeyReader[String]	= Converter total (_.value)
	given IntKeyReader:JsonKeyReader[Int]		= StringKeyReader >=> NS.StringToInt
	given LongKeyReader:JsonKeyReader[Long]		= StringKeyReader >=> NS.StringToLong
	given BigIntKeyReader:JsonKeyReader[BigInt]	= StringKeyReader >=> NS.StringToBigInt
	given UnitKeyReader:JsonKeyReader[Unit]		= StringKeyReader >=> NS.StringToUnit
}
