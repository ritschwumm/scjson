package scjson.converter

import scutil.lang.*

trait PrimitiveJsonKeyReaders {
	given StringKeyReader:JsonKeyReader[String]	= Converter.total(_.value)
	given IntKeyReader:JsonKeyReader[Int]		= StringKeyReader >=> NumberStringConverters.StringToInt
	given LongKeyReader:JsonKeyReader[Long]		= StringKeyReader >=> NumberStringConverters.StringToLong
	given BigIntKeyReader:JsonKeyReader[BigInt]	= StringKeyReader >=> NumberStringConverters.StringToBigInt
	given UnitKeyReader:JsonKeyReader[Unit]		= StringKeyReader >=> NumberStringConverters.StringToUnit
}
