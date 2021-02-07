package scjson.ast

object JsonVisitor {
	object asNull extends JsonVisitor[Option[Unit]]	{
		def onNull():Option[Unit]									= Some(())
		def onBoolean(value:Boolean):Option[Unit]					= None
		def onNumber(value:BigDecimal):Option[Unit]					= None
		def onString(value:String):Option[Unit]						= None
		def onArray(items:Seq[JsonValue]):Option[Unit]				= None
		def onObject(fields:Seq[(String, JsonValue)]):Option[Unit]	= None
	}

	object asBoolean extends JsonVisitor[Option[Boolean]]{
		def onNull():Option[Boolean]									= None
		def onBoolean(value:Boolean):Option[Boolean]					= Some(value)
		def onNumber(value:BigDecimal):Option[Boolean]					= None
		def onString(value:String):Option[Boolean]						= None
		def onArray(items:Seq[JsonValue]):Option[Boolean]				= None
		def onObject(fields:Seq[(String, JsonValue)]):Option[Boolean]	= None
	}

	object asNumber extends JsonVisitor[Option[BigDecimal]] {
		def onNull():Option[BigDecimal]										= None
		def onBoolean(value:Boolean):Option[BigDecimal]						= None
		def onNumber(value:BigDecimal):Option[BigDecimal]					= Some(value)
		def onString(value:String):Option[BigDecimal]						= None
		def onArray(items:Seq[JsonValue]):Option[BigDecimal]				= None
		def onObject(fields:Seq[(String, JsonValue)]):Option[BigDecimal]	= None
	}

	object asString extends JsonVisitor[Option[String]] {
		def onNull():Option[String]										= None
		def onBoolean(value:Boolean):Option[String]						= None
		def onNumber(value:BigDecimal):Option[String]					= None
		def onString(value:String):Option[String]						= Some(value)
		def onArray(items:Seq[JsonValue]):Option[String]				= None
		def onObject(fields:Seq[(String, JsonValue)]):Option[String]	= None
	}

	object asArray extends JsonVisitor[Option[Seq[JsonValue]]] {
		def onNull():Option[Seq[JsonValue]]										= None
		def onBoolean(value:Boolean):Option[Seq[JsonValue]]						= None
		def onNumber(value:BigDecimal):Option[Seq[JsonValue]]					= None
		def onString(value:String):Option[Seq[JsonValue]]						= None
		def onArray(items:Seq[JsonValue]):Option[Seq[JsonValue]]				= Some(items)
		def onObject(fields:Seq[(String, JsonValue)]):Option[Seq[JsonValue]]	= None
	}

	object asObject extends JsonVisitor[Option[Seq[(String,JsonValue)]]] {
		def onNull():Option[Seq[(String,JsonValue)]]									= None
		def onBoolean(value:Boolean):Option[Seq[(String,JsonValue)]]					= None
		def onNumber(value:BigDecimal):Option[Seq[(String,JsonValue)]]					= None
		def onString(value:String):Option[Seq[(String,JsonValue)]]						= None
		def onArray(items:Seq[JsonValue]):Option[Seq[(String,JsonValue)]]				= None
		def onObject(fields:Seq[(String, JsonValue)]):Option[Seq[(String,JsonValue)]]	= Some(fields)
	}
}

trait JsonVisitor[T] {
	def onNull():T
	def onBoolean(value:Boolean):T
	def onNumber(value:BigDecimal):T
	def onString(value:String):T
	def onArray(items:Seq[JsonValue]):T
	def onObject(fields:Seq[(String, JsonValue)]):T
}
