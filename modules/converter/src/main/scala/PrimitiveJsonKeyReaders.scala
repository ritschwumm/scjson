package scjson.converter

import scutil.lang._

import scjson.converter.{
	NumberStringConverters	=> NS
}

trait PrimitiveJsonKeyReaders {
	implicit val StringKeyReader:JsonKeyReader[String]	= Converter total (_.value)
	implicit val IntKeyReader:JsonKeyReader[Int]		= StringKeyReader >=> NS.StringToInt
	implicit val LongKeyReader:JsonKeyReader[Long]		= StringKeyReader >=> NS.StringToLong
	implicit val BigIntKeyReader:JsonKeyReader[BigInt]	= StringKeyReader >=> NS.StringToBigInt
	implicit val UnitKeyReader:JsonKeyReader[Unit]		= StringKeyReader >=> NS.StringToUnit
}
