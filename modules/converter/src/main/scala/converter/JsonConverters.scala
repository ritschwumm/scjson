package scjson.converter

import scutil.lang.implicits.*
import scutil.lang.*
import scjson.ast.*

object JsonConverters {
	val expectNull:JsonConverter[JsonValue,Unit]	=
		Converter.optional(_.asNull, JsonError(show"expected json null"))

	val expectBoolean:JsonConverter[JsonValue,Boolean]	=
		Converter.optional(_.asBoolean, JsonError(show"expected json boolean"))

	val expectNumber:JsonConverter[JsonValue,BigDecimal]	=
		Converter.optional(_.asNumber, JsonError(show"expected json number"))

	val expectString:JsonConverter[JsonValue,String]	=
		Converter.optional(_.asString, JsonError(show"expected json string"))

	val expectArray:JsonConverter[JsonValue,Seq[JsonValue]]	=
		Converter.optional(_.asArray, JsonError(show"expected json array"))

	val expectObject:JsonConverter[JsonValue,Seq[(String,JsonValue)]]	=
		Converter.optional(_.asObject, JsonError(show"expected json object"))

	//------------------------------------------------------------------------------

	val expectObjectMap:JsonConverter[JsonValue,Map[String,JsonValue]]	=
		expectObject map (_.toMap)

	//------------------------------------------------------------------------------

	val makeNull:JsonConverter[Unit,JsonValue]	=
		Converter pure JsonValue.Null

	val makeBoolean:JsonConverter[Boolean,JsonValue]	=
		Converter total JsonValue.fromBoolean

	val makeNumber:JsonConverter[BigDecimal,JsonValue]	=
		Converter total JsonValue.fromBigDecimal

	val makeString:JsonConverter[String,JsonValue]	=
		Converter total JsonValue.fromString

	val makeArray:JsonConverter[Seq[JsonValue],JsonValue]	=
		Converter total JsonValue.fromItems

	val makeObject:JsonConverter[Seq[(String,JsonValue)],JsonValue]	=
		Converter total JsonValue.fromFields

	//------------------------------------------------------------------------------

	val makeObjectMap:JsonConverter[Map[String,JsonValue],JsonValue]	=
		makeObject contraMap { _.toVector }

	//------------------------------------------------------------------------------

	def liftNull(sub:JsonConverter[Unit,Unit]):JsonConverter[JsonValue,JsonValue]	=
		expectNull >=> sub >=> makeNull

	def liftBoolean(sub:JsonConverter[Boolean,Boolean]):JsonConverter[JsonValue,JsonValue]	=
		expectBoolean >=> sub >=> makeBoolean

	def liftNumber(sub:JsonConverter[BigDecimal,BigDecimal]):JsonConverter[JsonValue,JsonValue]	=
		expectNumber >=> sub >=> makeNumber

	def liftString(sub:JsonConverter[String,String]):JsonConverter[JsonValue,JsonValue]	=
		expectString >=> sub >=> makeString

	def liftArray(sub:JsonConverter[Seq[JsonValue],Seq[JsonValue]]):JsonConverter[JsonValue,JsonValue]	=
		expectArray >=> sub >=> makeArray

	def liftObject(sub:JsonConverter[Seq[(String,JsonValue)],Seq[(String,JsonValue)]]):JsonConverter[JsonValue,JsonValue]	=
		expectObject >=> sub >=> makeObject

	def liftObjectMap(sub:JsonConverter[Map[String,JsonValue],Map[String,JsonValue]]):JsonConverter[JsonValue,JsonValue]	=
		expectObjectMap >=> sub >=> makeObjectMap
}
