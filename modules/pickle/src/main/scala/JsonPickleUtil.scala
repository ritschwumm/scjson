package scjson.pickle

import scjson.ast._

object JsonPickleUtil {
	def fail(message:String):Nothing =
		throw new JsonUnpickleException(JsonUnpickleFailure(message))

	//------------------------------------------------------------------------------

	def objectMap(it:JsonValue):Map[String,JsonValue]	=
		objectValue(it).toMap

	def objectValue(it:JsonValue):Seq[(String,JsonValue)]	=
		it.asObject.getOrElse(fail("expected a json object"))

	def arrayValue(it:JsonValue):Seq[JsonValue]	=
		it.asArray.getOrElse(fail("expected a json array"))

	//------------------------------------------------------------------------------

	def stringValue(it:JsonValue):String	=
		it.asString.getOrElse(fail("expected a json string"))

	def numberValue(it:JsonValue):BigDecimal	=
		it.asNumber.getOrElse(fail("expected a json number"))

	def booleanValue(it:JsonValue):Boolean	=
		it.asBoolean.getOrElse(fail("expected a json boolean"))

	//------------------------------------------------------------------------------

	def requireField(map:Map[String,JsonValue], key:String):JsonValue	=
		map get key match {
			case Some(x)	=> x
			case None		=> fail("expected a field " + key)
		}

	//------------------------------------------------------------------------------

	def unapplyTotal[S,T](unapply:S=>Option[T], value:S):T	=
		unapply(value) match {
			case Some(x)	=> x
			case None		=> sys error "expected unapply to be total"
		}
}
