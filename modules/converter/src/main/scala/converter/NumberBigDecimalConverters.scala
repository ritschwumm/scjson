package scjson.converter

import scutil.core.implicits.*
import scutil.lang.*

object NumberBigDecimalConverters {
	val IntToBigDecimal:JsonConverter[Int,BigDecimal]	=
		Converter total { it => BigDecimal exact it.toLong }

	val BigDecimalToInt:JsonConverter[BigDecimal,Int]	=
		Converter { it =>
			try {
				Validated.valid(it.toIntExact)
			}
			catch { case e:ArithmeticException =>
				// fractional or not fitting
				JsonInvalid(show"${it} is not an Int")
			}
		}

	//------------------------------------------------------------------------------

	val LongToBigDecimal:JsonConverter[Long,BigDecimal]	=
		Converter.total(BigDecimal.exact)

	val BigDecimalToLong:JsonConverter[BigDecimal,Long]	=
		Converter { it =>
			try {
				Validated.valid(it.toLongExact)
			}
			catch { case e:ArithmeticException =>
				// fractional or not fitting
				JsonInvalid(show"${it} is not a Long")
			}
		}

	//------------------------------------------------------------------------------

	val BigIntToBigDecimal:JsonConverter[BigInt,BigDecimal]	=
		Converter.total(BigDecimal.exact)

	val BigDecimalToBigInt:JsonConverter[BigDecimal,BigInt]	=
		Converter { it =>
			// fractional
			it.toBigIntExact.toValid(JsonError(show"${it} is not a BigInt"))
		}

	//------------------------------------------------------------------------------

	val FloatToBigDecimal:JsonConverter[Float,BigDecimal]	=
		Converter { it =>
			try {
				Validated.valid(BigDecimal.exact(it.toDouble))
			}
			catch { case e:NumberFormatException =>
				// infinite or NaN
				JsonInvalid(show"$it is not a BigDecimal")
			}
		}

	// NOTE might return Double.NEGATIVE_INFINITY or Double.POSITIVE_INFINITY e
	val BigDecimalToFloat:JsonConverter[BigDecimal,Float]	=
		Converter.total(_.toFloat)

	//------------------------------------------------------------------------------

	val DoubleToBigDecimal:JsonConverter[Double,BigDecimal]	=
		Converter { it =>
			try {
				Validated.valid(BigDecimal.exact(it))
			}
			catch { case e:NumberFormatException =>
				// infinite or NaN
				JsonInvalid(show"$it is not a BigDecimal")
			}
		}

	// NOTE might return Double.NEGATIVE_INFINITY or Double.POSITIVE_INFINITY
	val BigDecimalToDouble:JsonConverter[BigDecimal,Double]	=
		Converter.total(_.toDouble)
}
