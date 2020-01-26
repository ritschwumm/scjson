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

	// NOTE this supports the format used in scjson-pickle's CaseClassProtocol.caseClassSumFormat

	private val typeTag	= ""

	// converts old { "": "type", ...foo } to new { "type": { ...foo }}
	def extractTag:JsonConverter[JsonValue,JsonValue]	=
		JC.expectObject >=>
		Converter { parts =>
			val map 	= parts.toMap
			for {
				tagVal	<-	map get typeTag	toGood JsonError("type tag not found")
				tagStr	<-	tagVal.asString	toGood JsonError("type tag not a string")
			}
			yield {
				val remainder	= map - typeTag
				JsonObject.Var(tagStr -> JsonValue.mkObject(remainder.toVector))
			}
		}

	// converts new { "type": { ...foo }} to old { "": "type", ...foo }
	def injectTag:JsonConverter[JsonValue,JsonValue]	=
		JC.expectObject >=>
		Converter { it =>
			for {
				item	<-	it.singleOption	toGood JsonError(show"expected exactly one element, found ${it.size}")
				(k, v)	=	item
				payload	<-	v.asObject		toGood JsonError(show"expected payload to be an object")
			}
			yield {
				JsonValue.mkObject(
					payload	:+ (typeTag -> JsonValue.mkString(k))
				)
			}
		}
}
