package scjson.converter

import scjson.converter.{
	JsonConverters				=> JC,
	NumberBigDecimalConverters	=> NB
}

trait PrimitiveJsonWriters {
	given StringWriter:JsonWriter[String]	=
		JC.makeString

	given BooleanWriter:JsonWriter[Boolean]	=
		JC.makeBoolean

	given IntWriter:JsonWriter[Int]	=
		NB.IntToBigDecimal >=> JC.makeNumber

	given BigIntWriter:JsonWriter[BigInt]	=
		NB.BigIntToBigDecimal >=> JC.makeNumber

	given LongWriter:JsonWriter[Long]	=
		NB.LongToBigDecimal >=> JC.makeNumber

	given FloatWriter:JsonWriter[Float]	=
		NB.FloatToBigDecimal >=> JC.makeNumber

	given DoubleWriter:JsonWriter[Double]	=
		NB.DoubleToBigDecimal >=> JC.makeNumber

	given BigDecimalWriter:JsonWriter[BigDecimal]	=
		JC.makeNumber
}
