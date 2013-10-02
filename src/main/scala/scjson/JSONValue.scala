package scjson

sealed abstract class JSONValue {
	def downcast[T<:JSONValue]:Option[T]	=
			try { Some(asInstanceOf[T]) }
			catch { case e:ClassCastException => None }
}

case object JSONNull extends JSONValue

object JSONBoolean {
	def apply(value:Boolean):JSONBoolean			= if (value) JSONTrue else JSONFalse
	def unapply(value:JSONBoolean):Option[Boolean]	= Some(value.value)
}
sealed abstract class JSONBoolean extends JSONValue {
	def value:Boolean	= this match {
		case JSONTrue	=> true
		case JSONFalse	=> false
	}
}
case object JSONTrue	extends JSONBoolean
case object JSONFalse	extends JSONBoolean

object JSONNumber {
	def apply(value:Int):JSONNumber		= JSONNumber(BigDecimal(value))
	def apply(value:Long):JSONNumber	= JSONNumber(BigDecimal(value))
	def apply(value:Float):JSONNumber	= JSONNumber(BigDecimal(value))
	def apply(value:Double):JSONNumber	= JSONNumber(BigDecimal(value))
	def apply(value:BigInt):JSONNumber	= JSONNumber(BigDecimal(value))
}
case class JSONNumber(value:BigDecimal)	extends JSONValue

case class JSONString(value:String)		extends JSONValue

object JSONArray {
	val empty	= JSONVarArray()
}
case class JSONArray(value:Seq[JSONValue])	extends JSONValue {
	def get(index:Int):Option[JSONValue]	= value lift index
	def ++ (that:JSONArray):JSONArray		= JSONArray(this.value ++ that.value)
}

object JSONObject {
	val empty	= JSONObject(Seq.empty)
}
case class JSONObject(value:Seq[(String,JSONValue)])	extends JSONValue {
	def get(key:String):Option[JSONValue]	= value collectFirst { case (k,v) if (k == key) => v }
	def ++ (that:JSONObject):JSONObject		= JSONObject(this.value ++ that.value)
	def valueMap:Map[String,JSONValue]		= value.toMap
}
