package scjson.converter

import scutil.base.implicits._
import scutil.lang._
import scjson.ast._
import scjson.converter.{ JsonConverters => JC }

// BETTER json rework
object SumConverters {
	val expectTagged:JsonConverter[JsonValue,(String,JsonValue)]	=
		JC.expectObject >=>
		Converter { it =>
			it.singleOption toGood JsonError(show"expected exactly one element, found ${it.size}")
		}

	val makeTagged:JsonConverter[(String,JsonValue),JsonValue]	=
		(Converter total { kv:(String,JsonValue) => Seq(kv) })	>=>
		JC.makeObject

	//------------------------------------------------------------------------------

	def sumReader[T](partials:Seq[(String,JsonReader[T])]):JsonReader[T]	=
		expectTagged >=>
		Converter { case (k, v) =>
			partials
			.collectFirst	{ case (`k`, conv) => conv convert v }
			.getOrElse		(Validated bad JsonError(show"unexpected key $k"))
		}

	def sumWriter[T](partials:Seq[(String,JsonConverter[T,Option[JsonValue]])]):JsonWriter[T]	=
		(Converter { (it:T) =>
			val a:JsonResult[Seq[Option[(String,JsonValue)]]]	=
				partials traverseValidated { case (k, conv) =>
					conv convert it map { _ map ((k, _)) }
				}

			val b:JsonResult[Seq[(String,JsonValue)]]	=
				a map { _.collapse }

			val c:JsonResult[(String,JsonValue)]	=
				b flatMap { pairs =>
					pairs.singleEither
					.leftMap {
						case false	=> JsonError(show"expected unique ctor, found none")
						case true	=> JsonError(show"expected unique ctor, found multiple")
					}
					.toValidated
				}

			c
		}) >=>
		makeTagged

	//------------------------------------------------------------------------------

	def subReader[Super,Sub<:Super](reader:JsonReader[Sub]):JsonReader[Super]	=
		Converter(reader.convert)

	def subWriter[Super,Sub<:Super](writer:JsonWriter[Sub], ctor:PFunction[Super,Sub]):JsonConverter[Super,Option[JsonValue]]	=
		Converter(ctor(_) traverseValidated writer.convert)
}
