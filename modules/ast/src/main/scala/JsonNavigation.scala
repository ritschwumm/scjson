package scjson.ast

@SuppressWarnings(Array("org.wartremover.warts.Overloading"))
object JsonNavigation {
	extension (value:JsonValue)	{
		def navigation	= new JsonNavigation(Some(value))
		export navigation.*
	}

	extension (value:Option[JsonValue])	{
		def navigation	= new JsonNavigation(value)
		export navigation.*
	}
}

@SuppressWarnings(Array("org.wartremover.warts.Overloading"))
final class JsonNavigation(peer:Option[JsonValue]) {
	def /(name:String):Option[JsonValue]	= toMap.flatMap(_.get(name))
	def /(index:Int):Option[JsonValue]		= arraySeq.flatMap(_.lift(index))

	//------------------------------------------------------------------------------

	def nullRef:Option[Unit]						= peer.flatMap(_.asNull)
	def string:Option[String]						= peer.flatMap(_.asString)
	def boolean:Option[Boolean]						= peer.flatMap(_.asBoolean)
	def number:Option[BigDecimal]					= peer.flatMap(_.asNumber)
	def arraySeq:Option[Seq[JsonValue]]				= peer.flatMap(_.asArray)
	def objectSeq:Option[Seq[(String,JsonValue)]]	= peer.flatMap(_.asObject)

	//------------------------------------------------------------------------------

	def toLong:Option[Long]							= number.map(_.longValue)
	def toInt:Option[Int]							= number.map(_.intValue)
	def toDouble:Option[Double]						= number.map(_.doubleValue)
	def toFloat:Option[Float]						= number.map(_.floatValue)
	def toMap:Option[Map[String,JsonValue]]			= objectSeq.map(_.toMap)
}
