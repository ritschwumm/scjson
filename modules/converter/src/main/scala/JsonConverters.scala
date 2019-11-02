package scjson.converter

import scutil.lang.implicits._
import scutil.lang._
import scjson.ast._

object JsonConverters {
	val expectNull:JsonConverter[JsonValue,Unit]	=
			Converter optional (_.asNull, JsonError(show"expected JsonNull"))

	val expectBoolean:JsonConverter[JsonValue,Boolean]	=
			Converter optional (_.asBoolean, JsonError(show"expected JsonBoolean"))

	val expectNumber:JsonConverter[JsonValue,BigDecimal]	=
			Converter optional (_.asNumber, JsonError(show"expected JsonNumber"))

	val expectString:JsonConverter[JsonValue,String]	=
			Converter optional (_.asString, JsonError(show"expected JsonString"))

	val expectArray:JsonConverter[JsonValue,Seq[JsonValue]]	=
			Converter optional (_.asArray, JsonError(show"expected JsonArray"))

	val expectObject:JsonConverter[JsonValue,Seq[(String,JsonValue)]]	=
			Converter optional (_.asObject, JsonError(show"expected JsonObject"))

	//------------------------------------------------------------------------------

	val expectObjectMap:JsonConverter[JsonValue,Map[String,JsonValue]]	=
			expectObject map (_.toMap)

	//------------------------------------------------------------------------------

	val makeNull:JsonConverter[Unit,JsonValue]	=
			Converter total JsonValue.mkNull

	val makeBoolean:JsonConverter[Boolean,JsonValue]	=
			Converter total JsonValue.mkBoolean

	val makeNumber:JsonConverter[BigDecimal,JsonValue]	=
			Converter total JsonValue.mkNumber

	val makeString:JsonConverter[String,JsonValue]	=
			Converter total JsonValue.mkString

	val makeArray:JsonConverter[Seq[JsonValue],JsonValue]	=
			Converter total JsonValue.mkArray

	val makeObject:JsonConverter[Seq[(String,JsonValue)],JsonValue]	=
			Converter total JsonValue.mkObject

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
