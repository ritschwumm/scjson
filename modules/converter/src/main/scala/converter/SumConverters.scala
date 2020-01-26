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
}
