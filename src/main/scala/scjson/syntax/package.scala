package scjson

import scjson.serialization._

package object syntax {
	// NOTE must not be passed null values
	
	def jsonArray(values:JSONWrapper*):JSONArray	=
			JSONArray(values.toVector map { _.unwrap })
		
	def jsonObject(values:(String,JSONWrapper)*):JSONObject	=
			JSONObject(values.toVector map { case (k, v) => (k, v.unwrap) })
		
	def jsonSimple(value:JSONWrapper):JSONValue	=
			value.unwrap
		
	//------------------------------------------------------------------------------
	
	final case class JSONWrapper(unwrap:JSONValue)
	
	implicit def toJSONWrapper[T:Format](it:T):JSONWrapper	=
			new JSONWrapper(format[T] write it)
}
