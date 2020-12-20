package scjson.converter

import scutil.core.implicits._
import scutil.lang._

object NumberStringConverters {
	val UnitToString:JsonConverter[Unit,String]	=
		Converter constant ""

	val StringToUnit:JsonConverter[String,Unit]	=
		Converter { it => (it == "").validated(JsonError(show"$it is not a Unit"), ()) }

	//------------------------------------------------------------------------------

	val IntToString:JsonConverter[Int,String]	=
		Converter total (_.toString)

	val StringToInt:JsonConverter[String,Int]	=
		Converter { it => it.toIntOption toValid JsonError(show"$it is not an Int") }

	//------------------------------------------------------------------------------

	val LongToString:JsonConverter[Long,String]	=
		Converter total (_.toString)

	val StringToLong:JsonConverter[String,Long]	=
		Converter { it => it.toLongOption toValid JsonError(show"$it is not a Long") }

	//------------------------------------------------------------------------------

	val BigIntToString:JsonConverter[BigInt,String]	=
		Converter total (_.toString)

	val StringToBigInt:JsonConverter[String,BigInt]	=
		Converter { it => it.toBigIntOption toValid JsonError(show"$it is not a BigInt") }
}
