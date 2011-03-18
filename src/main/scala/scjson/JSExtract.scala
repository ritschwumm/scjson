package scjson

// TODO see dispatch.json._

object JSExtract {
	implicit def extendJSExtract(value:Option[JSValue]):JSExtract	= new JSExtract(value)
	implicit def extendJSExtract(value:JSValue):JSExtract			= new JSExtract(Some(value))
}

final class JSExtract(value:Option[JSValue]) {
	def /(name:String):Option[JSValue]	= objectMap flatMap { _ get JSString(name) }
	def /(index:Int):Option[JSValue]	= arraySeq  flatMap { _.lift apply index }
		
	def first:Option[JSValue] = step {
		case JSObject(data)	=> data.values.headOption
		case JSArray(data)	=> data.headOption
	} flatMap identity
	
	def nullRef:Option[Null]		= step { case JSNull		 => null	}
	def string:Option[String]		= step { case JSString(data) => data	}
	def decimal:Option[BigDecimal]	= step { case JSNumber(data) => data	}
	def long:Option[Long]			= step { case JSNumber(data) => data.longValue		}
	def int:Option[Int]				= step { case JSNumber(data) => data.intValue		}
	def double:Option[Double]		= step { case JSNumber(data) => data.doubleValue	}
	def float:Option[Float]			= step { case JSNumber(data) => data.floatValue		}
	def boolean:Option[Boolean]		= step { 
		case JSTrue		=> true
		case JSFalse	=> false
	}
	def arraySeq:Option[Seq[JSValue]]			= step { case JSArray(data)		=> data }
	def objectMap:Option[Map[JSString,JSValue]]	= step { case JSObject(data)	=> data }
	
	def step[T](pf:PartialFunction[JSValue,T]):Option[T] = value flatMap pf.lift
}
