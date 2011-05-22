package scjson

object JSNavigation {
	implicit def extendJSNavigation(value:Option[JSValue]):JSNavigation	= new JSNavigation(value)
	implicit def extendJSNavigation(value:JSValue):JSNavigation			= new JSNavigation(Some(value))
}

final class JSNavigation(value:Option[JSValue]) {
	def /(name:String):Option[JSValue]	= objectMap flatMap { _ get JSString(name) }
	def /(index:Int):Option[JSValue]	= arraySeq  flatMap { _.lift apply index }
		
	def first:Option[JSValue] = 
			value collect {
				case JSObject(data)	=> data.values.headOption
				case JSArray(data)	=> data.headOption
			} flatMap identity
			
	def nullRef:Option[Null]		= value collect { case JSNull			=> null	}
	def string:Option[String]		= value collect { case JSString(data)	=> data	}
	def decimal:Option[BigDecimal]	= value collect { case JSNumber(data)	=> data	}
	def long:Option[Long]			= value collect { case JSNumber(data)	=> data.longValue	}
	def int:Option[Int]				= value collect { case JSNumber(data)	=> data.intValue	}
	def double:Option[Double]		= value collect { case JSNumber(data)	=> data.doubleValue	}
	def float:Option[Float]			= value collect { case JSNumber(data)	=> data.floatValue	}
	def boolean:Option[Boolean]		= value collect { case JSTrue			=> true 
													  case JSFalse			=> false }
	def arraySeq:Option[Seq[JSValue]]			= value collect { case JSArray(data)	=> data }
	def objectMap:Option[Map[JSString,JSValue]]	= value collect { case JSObject(data)	=> data }
}
