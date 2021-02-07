package scjson.pickle

import scjson.ast._

object JsonPickleUtil {
	def fail(message:String):Nothing =
		throw new JsonUnpickleException(JsonUnpickleFailure(message))

	// TODO add expected and actual ctor name
	@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
	@deprecated("will be removed", "0.226.0")
	def downcast[T<:JsonValue](it:JsonValue):T	=
		try { it.asInstanceOf[T] }
		catch { case e:ClassCastException => fail("unexpected json value type") }

	//------------------------------------------------------------------------------

	def objectMap(it:JsonValue):Map[String,JsonValue]	=
		it match {
			case JsonObject(value)	=> value.toMap
			case _					=> fail("expected a json object")
		}

	def objectValue(it:JsonValue):Seq[(String,JsonValue)]	=
		it match {
			case JsonObject(value)	=> value
			case _					=> fail("expected a json object")
		}

	def arrayValue(it:JsonValue):Seq[JsonValue]	=
		it match {
			case JsonArray(value)	=> value
			case _					=> fail("expected a json array")
		}

	//------------------------------------------------------------------------------

	def stringValue(it:JsonValue):String	=
		it match {
			case JsonString(value)	=> value
			case _					=> fail("expected a json string")
		}

	def numberValue(it:JsonValue):BigDecimal	=
		it match {
			case JsonNumber(value)	=> value
			case _					=> fail("expected a json number")
		}

	def booleanValue(it:JsonValue):Boolean	=
		it match {
			case JsonBoolean(value)	=> value
			case _					=> fail("expected a json boolean")
		}

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
