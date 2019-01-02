package scjson.pickle

import scutil.lang.ISeq

import scjson.ast._

object JsonPickleUtil {
	def fail(message:String):Nothing =
			throw new JsonUnpickleException(JsonUnpickleFailure(message))

	// TODO add expected and actual ctor name
	@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
	def downcast[T<:JsonValue](it:JsonValue):T	=
			try { it.asInstanceOf[T] }
			catch { case e:ClassCastException => fail("unexpected json value type") }

	//------------------------------------------------------------------------------

	def objectMap(it:JsonValue):Map[String,JsonValue]	=
			it match {
				case JsonObject(value)	=> value.toMap
				case _					=> fail("expected a JsonObject")
			}

	def objectValue(it:JsonValue):ISeq[(String,JsonValue)]	=
			it match {
				case JsonObject(value)	=> value
				case _					=> fail("expected a JsonObject")
			}

	def arrayValue(it:JsonValue):ISeq[JsonValue]	=
			it match {
				case JsonArray(value)	=> value
				case _					=> fail("expected a JsonArray")
			}

	//------------------------------------------------------------------------------

	def stringValue(it:JsonValue):String	=
			it match {
				case JsonString(value)	=> value
				case _					=> fail("expected a JsonString")
			}

	def numberValue(it:JsonValue):BigDecimal	=
			it match {
				case JsonNumber(value)	=> value
				case _					=> fail("expected a JsonNumber")
			}

	def booleanValue(it:JsonValue):Boolean	=
			it match {
				case JsonBoolean(value)	=> value
				case _					=> fail("expected a JsonBoolean")
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
