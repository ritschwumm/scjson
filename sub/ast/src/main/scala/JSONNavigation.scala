package scjson.ast

import scutil.lang._

object JSONNavigation {
	implicit def extendJSONNavigation(value:JSONValue):JSONNavigation			= new JSONNavigation(Some(value))
	implicit def extendJSONNavigation(value:Option[JSONValue]):JSONNavigation	= new JSONNavigation(value)
}

final class JSONNavigation(peer:Option[JSONValue]) {
	def /(name:String):Option[JSONValue]	= toMap		flatMap { _ get		name	}
	def /(index:Int):Option[JSONValue]		= arraySeq  flatMap { _ lift	index	}
	
	//------------------------------------------------------------------------------
			
	def nullRef:Option[Unit]						= peer	flatMap { _.asNull		}
	def string:Option[String]						= peer	flatMap { _.asString	}
	def boolean:Option[Boolean]						= peer	flatMap { _.asBoolean	}
	def number:Option[BigDecimal]					= peer	flatMap { _.asNumber	}
	def arraySeq:Option[ISeq[JSONValue]]			= peer	flatMap { _.asArray		}
	def objectSeq:Option[ISeq[(String,JSONValue)]]	= peer	flatMap { _.asObject	}
	
	//------------------------------------------------------------------------------
	
	def toLong:Option[Long]							= number	map { _.longValue	}
	def toInt:Option[Int]							= number	map { _.intValue	}
	def toDouble:Option[Double]						= number	map { _.doubleValue	}
	def toFloat:Option[Float]						= number	map { _.floatValue	}
	def toMap:Option[Map[String,JSONValue]]			= objectSeq map { _.toMap		}
}
