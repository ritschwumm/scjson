package scjson.ast

import scutil.lang._

object JSONNavigation {
	implicit def extendJSONNavigation(value:JSONValue):JSONNavigation			= new JSONNavigation(Some(value))
	implicit def extendJSONNavigation(value:Option[JSONValue]):JSONNavigation	= new JSONNavigation(value)
}

final class JSONNavigation(peer:Option[JSONValue]) {
	def /(name:String):Option[JSONValue]	= objectMap flatMap { _ get		name	}
	def /(index:Int):Option[JSONValue]		= arraySeq  flatMap { _ lift	index	}
	
	def downcast[T<:JSONValue](subtype:Subtype[JSONValue,T]):Option[T]	=
			peer flatMap subtype.unapply
	
	//------------------------------------------------------------------------------
			
	def jsonNull:Option[JSONNull.type]	= downcast(JSONSubtypes.jsonNull)
	def jsonString:Option[JSONString]	= downcast(JSONSubtypes.jsonString)
	def jsonNumber:Option[JSONNumber]	= downcast(JSONSubtypes.jsonNumber)
	def jsonBoolean:Option[JSONBoolean]	= downcast(JSONSubtypes.jsonBoolean)
	def jsonArray:Option[JSONArray]		= downcast(JSONSubtypes.jsonArray)
	def jsonObject:Option[JSONObject]	= downcast(JSONSubtypes.jsonObject)
	
	//------------------------------------------------------------------------------
	
	def nullRef:Option[Null]						= downcast(JSONSubtypes.jsonNull)	map	JSONBijections.jsonNull.write
	def string:Option[String]						= downcast(JSONSubtypes.jsonString)	map JSONBijections.jsonString.write
	def decimal:Option[BigDecimal]					= downcast(JSONSubtypes.jsonNumber)	map JSONBijections.jsonNumber.write
	def long:Option[Long]							= decimal map { _.longValue		}
	def int:Option[Int]								= decimal map { _.intValue		}
	def double:Option[Double]						= decimal map { _.doubleValue	}
	def float:Option[Float]							= decimal map { _.floatValue	}
	def boolean:Option[Boolean]						= downcast(JSONSubtypes.jsonBoolean)	map JSONBijections.jsonBoolean.write
	def arraySeq:Option[ISeq[JSONValue]]			= downcast(JSONSubtypes.jsonArray)		map JSONBijections.jsonArray.write
	def objectSeq:Option[ISeq[(String,JSONValue)]]	= downcast(JSONSubtypes.jsonObject)		map JSONBijections.jsonObject.write
	def objectMap:Option[Map[String,JSONValue]]		= objectSeq map { _.toMap }
}
