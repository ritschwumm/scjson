package scjson.converter

import scjson.converter.{
	JsonConverters				=> JC,
	NumberBigDecimalConverters	=> NB
}

trait PrimitiveJsonReaders {
	given StringReader:JsonReader[String]	=
		JC.expectString

	given BooleanReader:JsonReader[Boolean]	=
		JC.expectBoolean

	given IntReader:JsonReader[Int]	=
		JC.expectNumber >=> NB.BigDecimalToInt

	given LongReader:JsonReader[Long]	=
		JC.expectNumber >=> NB.BigDecimalToLong

	given BigIntReader:JsonReader[BigInt]	=
		JC.expectNumber >=> NB.BigDecimalToBigInt

	given FloatReader:JsonReader[Float]	=
		JC.expectNumber >=> NB.BigDecimalToFloat

	given DoubleReader:JsonReader[Double]	=
		JC.expectNumber >=> NB.BigDecimalToDouble

	given BigDecimalReader:JsonReader[BigDecimal]	=
		JC.expectNumber
}
