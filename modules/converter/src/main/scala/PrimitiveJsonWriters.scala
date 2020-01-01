package scjson.converter

import scjson.converter.{
	JsonConverters				=> JC,
	NumberBigDecimalConverters	=> NB
}

trait PrimitiveJsonWriters {
	implicit val StringWriter:JsonWriter[String]	=
		JC.makeString

	implicit val BooleanWriter:JsonWriter[Boolean]	=
		JC.makeBoolean

	implicit val IntWriter:JsonWriter[Int]	=
		NB.IntToBigDecimal >=> JC.makeNumber

	implicit val BigIntWriter:JsonWriter[BigInt]	=
		NB.BigIntToBigDecimal >=> JC.makeNumber

	implicit val LongWriter:JsonWriter[Long]	=
		NB.LongToBigDecimal >=> JC.makeNumber

	implicit val FloatWriter:JsonWriter[Float]	=
		NB.FloatToBigDecimal >=> JC.makeNumber

	implicit val DoubleWriter:JsonWriter[Double]	=
		NB.DoubleToBigDecimal >=> JC.makeNumber

	implicit val BigDecimalWriter:JsonWriter[BigDecimal]	=
		JC.makeNumber
}
