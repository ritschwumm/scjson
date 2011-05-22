package scjson

sealed abstract class JSValue

case object JSNull extends JSValue

object JSBoolean {
	def apply(value:Boolean)	= if (value) JSTrue else JSFalse
}
sealed abstract class JSBoolean extends JSValue
case object JSTrue	extends JSBoolean
case object JSFalse	extends JSBoolean

object JSNumber {
	def apply(value:Int):JSNumber		= JSNumber(BigDecimal(value))
	def apply(value:Long):JSNumber		= JSNumber(BigDecimal(value))
	def apply(value:Float):JSNumber		= JSNumber(BigDecimal(value))
	def apply(value:Double):JSNumber	= JSNumber(BigDecimal(value))
	def apply(value:BigInt):JSNumber	= JSNumber(BigDecimal(value))
}
case class JSNumber(value:BigDecimal)				extends JSValue

case class JSString(value:String)					extends JSValue

case class JSArray(value:Seq[JSValue])				extends JSValue

case class JSObject(value:Map[JSString,JSValue])	extends JSValue
