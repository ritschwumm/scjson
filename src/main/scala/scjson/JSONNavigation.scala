package scjson

object JSONNavigation {
	implicit def extendJSONNavigation(value:JSONValue):JSONNavigation			= new JSONNavigation(Some(value))
	implicit def extendJSONNavigation(value:Option[JSONValue]):JSONNavigation	= new JSONNavigation(value)
}

final class JSONNavigation(value:Option[JSONValue]) {
	def /(name:String):Option[JSONValue]	= objectMap flatMap { _ get JSONString(name) }
	def /(index:Int):Option[JSONValue]		= arraySeq  flatMap { _.lift apply index }
	
	def first:Option[JSONValue] = 
			value collect {
				case JSONObject(data)	=> data.values.headOption
				case JSONArray(data)	=> data.headOption
			} flatMap identity
		
	//------------------------------------------------------------------------------
			
	def jsonNull:Option[JSONNull.type]	= value collect { case JSONNull			=> JSONNull	}
	def jsonString:Option[JSONString]	= value collect { case x:JSONString		=> x		}
	def jsonNumber:Option[JSONNumber]	= value collect { case x:JSONNumber		=> x		}
	def jsonBoolean:Option[JSONBoolean]	= value collect { case x:JSONBoolean	=> x		}
	def jsonArray:Option[JSONArray]		= value collect { case x:JSONArray		=> x		}
	def jsonObject:Option[JSONObject]	= value collect { case x:JSONObject		=> x		}
	
	//------------------------------------------------------------------------------
	
	def nullRef:Option[Null]						= value collect { case JSONNull				=> null	}
	def string:Option[String]						= value collect { case JSONString(data)		=> data	}
	def decimal:Option[BigDecimal]					= value collect { case JSONNumber(data)		=> data	}
	def long:Option[Long]							= value collect { case JSONNumber(data)		=> data.longValue	}
	def int:Option[Int]								= value collect { case JSONNumber(data)		=> data.intValue	}
	def double:Option[Double]						= value collect { case JSONNumber(data)		=> data.doubleValue	}
	def float:Option[Float]							= value collect { case JSONNumber(data)		=> data.floatValue	}
	def boolean:Option[Boolean]						= value collect { case JSONBoolean(data)	=> data				}
	def arraySeq:Option[Seq[JSONValue]]				= value collect { case JSONArray(data)		=> data }
	def objectMap:Option[Map[JSONString,JSONValue]]	= value collect { case JSONObject(data)		=> data }
}
