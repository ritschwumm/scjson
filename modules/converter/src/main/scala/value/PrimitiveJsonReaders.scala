package scjson.converter

trait PrimitiveJsonReaders {
	given StringReader:JsonReader[String]	=
		JsonConverters.expectString

	given BooleanReader:JsonReader[Boolean]	=
		JsonConverters.expectBoolean

	given IntReader:JsonReader[Int]	=
		JsonConverters.expectNumber >=> NumberBigDecimalConverters.BigDecimalToInt

	given LongReader:JsonReader[Long]	=
		JsonConverters.expectNumber >=> NumberBigDecimalConverters.BigDecimalToLong

	given BigIntReader:JsonReader[BigInt]	=
		JsonConverters.expectNumber >=> NumberBigDecimalConverters.BigDecimalToBigInt

	given FloatReader:JsonReader[Float]	=
		JsonConverters.expectNumber >=> NumberBigDecimalConverters.BigDecimalToFloat

	given DoubleReader:JsonReader[Double]	=
		JsonConverters.expectNumber >=> NumberBigDecimalConverters.BigDecimalToDouble

	given BigDecimalReader:JsonReader[BigDecimal]	=
		JsonConverters.expectNumber
}
