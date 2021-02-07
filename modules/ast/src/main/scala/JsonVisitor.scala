package scjson.ast

trait JsonVisitor[T] {
	def onNull():T
	def onBoolean(value:Boolean):T
	def onNumber(value:BigDecimal):T
	def onString(value:String):T
	def onArray(items:Seq[JsonValue]):T
	def onObject(fields:Seq[(String, JsonValue)]):T
}
