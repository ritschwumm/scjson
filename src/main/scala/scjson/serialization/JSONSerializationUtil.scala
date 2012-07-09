package scjson.serialization

import scjson._

private object JSONSerializationUtil {
	def fail(message:String):Nothing =
			throw new JSONDeserializationException(message)
	
	def fail(message:String, e:Exception):Nothing =
			throw new JSONDeserializationException(message, e)
	
	def downcast[T<:JSONValue](it:JSONValue):T	=
			try { it.asInstanceOf[T] }
			catch { case e:ClassCastException => fail("unexpected json value type", e) }
	
	//------------------------------------------------------------------------------
			
	def objectMap(v:JSONValue):Map[String,JSONValue]	= v match {
		case x:JSONObject	=> x.value.toMap
		case x				=> fail("expected a JSONObject")
	}
	
	def objectValue(v:JSONValue):Seq[(String,JSONValue)]	= v match {
		case x:JSONObject	=> x.value
		case x				=> fail("expected a JSONObject")
	}
	
	def arrayValue(v:JSONValue):Seq[JSONValue]	= v match {
		case x:JSONArray	=> x.value
		case x				=> fail("expected a JSONArray")
	}
	
	def stringValue(v:JSONValue):String	= v match {
		case x:JSONString	=> x.value
		case x				=> fail("expected a JSONString")
	}
	
	def numberValue(v:JSONValue):BigDecimal	= v match {
		case x:JSONNumber	=> x.value
		case x				=> fail("expected a JSONNumber")
	}
	
	def booleanValue(v:JSONValue):Boolean	= v match {
		case x:JSONBoolean	=> x.value
		case x				=> fail("expected a JSONBoolean")
	}
}
