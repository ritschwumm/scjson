package scjson.converter

import scjson.converter.{
	JsonConverters				=> JC,
	NumberBigDecimalConverters	=> NB
}

trait PrimitiveJsonReaders {
	implicit val StringReader:JsonReader[String]	=
			JC.expectString

	implicit val BooleanReader:JsonReader[Boolean]	=
			JC.expectBoolean

	implicit val IntReader:JsonReader[Int]	=
			JC.expectNumber >=> NB.BigDecimalToInt

	implicit val LongReader:JsonReader[Long]	=
			JC.expectNumber >=> NB.BigDecimalToLong

	implicit val BigIntReader:JsonReader[BigInt]	=
			JC.expectNumber >=> NB.BigDecimalToBigInt

	implicit val FloatReader:JsonReader[Float]	=
			JC.expectNumber >=> NB.BigDecimalToFloat

	implicit val DoubleReader:JsonReader[Double]	=
			JC.expectNumber >=> NB.BigDecimalToDouble

	implicit val BigDecimalReader:JsonReader[BigDecimal]	=
			JC.expectNumber
}
