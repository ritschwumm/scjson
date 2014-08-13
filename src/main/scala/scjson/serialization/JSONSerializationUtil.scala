package scjson.serialization

import scutil.lang.ISeq

import scjson._

object JSONSerializationUtil {
	def fail(message:String):Nothing =
			throw new JSONDeserializationException(message)
	
	def fail(message:String, e:Exception):Nothing =
			throw new JSONDeserializationException(message, e)
	
	def downcast[T<:JSONValue](it:JSONValue):T	=
			try { it.asInstanceOf[T] }
			catch { case e:ClassCastException => fail("unexpected json value type", e) }
	
	//------------------------------------------------------------------------------
			
	def objectMap(it:JSONValue):Map[String,JSONValue]	=
			it match {
				case JSONObject(value)	=> value.toMap
				case _					=> fail("expected a JSONObject")
			}
	
	def objectValue(it:JSONValue):ISeq[(String,JSONValue)]	=
			it match {
				case JSONObject(value)	=> value
				case _					=> fail("expected a JSONObject")
			}
	
	def arrayValue(it:JSONValue):ISeq[JSONValue]	=
			it match {
				case JSONArray(value)	=> value
				case _					=> fail("expected a JSONArray")
			}
	
	//------------------------------------------------------------------------------
	
	def stringValue(it:JSONValue):String	=
			it match {
				case JSONString(value)	=> value
				case _					=> fail("expected a JSONString")
			}
	
	def numberValue(it:JSONValue):BigDecimal	=
			it match {
				case JSONNumber(value)	=> value
				case _					=> fail("expected a JSONNumber")
			}
	
	def booleanValue(it:JSONValue):Boolean	=
			it match {
				case JSONBoolean(value)	=> value
				case _					=> fail("expected a JSONBoolean")
			}
}
