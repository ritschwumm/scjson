package scjson.pickle

import scutil.lang.ISeq

import scjson.ast._

object JSONPickleUtil {
	def fail(message:String):Nothing =
			throw new JSONUnpickleException(JSONUnpickleFailure(message))
	
	// TODO add expected and actual ctor name
	def downcast[T<:JSONValue](it:JSONValue):T	=
			try { it.asInstanceOf[T] }
			catch { case e:ClassCastException => fail("unexpected json value type") }
	
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
			
	//------------------------------------------------------------------------------
	
	def unapplyTotal[S,T](unapply:S=>Option[T], value:S):T	=
			unapply(value) match {
				case Some(x)	=> x
				case None		=> sys error "expected unapply to be total"
			}
}
