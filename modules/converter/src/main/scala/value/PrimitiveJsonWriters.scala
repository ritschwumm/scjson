package scjson.converter

trait PrimitiveJsonWriters {
	given StringWriter:JsonWriter[String]	=
		JsonConverters.makeString

	given BooleanWriter:JsonWriter[Boolean]	=
		JsonConverters.makeBoolean

	given IntWriter:JsonWriter[Int]	=
		NumberBigDecimalConverters.IntToBigDecimal >=> JsonConverters.makeNumber

	given BigIntWriter:JsonWriter[BigInt]	=
		NumberBigDecimalConverters.BigIntToBigDecimal >=> JsonConverters.makeNumber

	given LongWriter:JsonWriter[Long]	=
		NumberBigDecimalConverters.LongToBigDecimal >=> JsonConverters.makeNumber

	given FloatWriter:JsonWriter[Float]	=
		NumberBigDecimalConverters.FloatToBigDecimal >=> JsonConverters.makeNumber

	given DoubleWriter:JsonWriter[Double]	=
		NumberBigDecimalConverters.DoubleToBigDecimal >=> JsonConverters.makeNumber

	given BigDecimalWriter:JsonWriter[BigDecimal]	=
		JsonConverters.makeNumber
}
