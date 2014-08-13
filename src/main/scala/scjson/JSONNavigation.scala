package scjson

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
	
	def extract[S<:JSONValue,T](subtype:Subtype[JSONValue,S], bijection:Bijection[S,T]):Option[T]	=
			downcast(subtype) map bijection.write
	
	//------------------------------------------------------------------------------
			
	def jsonNull:Option[JSONNull.type]	= downcast(JSONSubtypes.jsonNull)
	def jsonString:Option[JSONString]	= downcast(JSONSubtypes.jsonString)
	def jsonNumber:Option[JSONNumber]	= downcast(JSONSubtypes.jsonNumber)
	def jsonBoolean:Option[JSONBoolean]	= downcast(JSONSubtypes.jsonBoolean)
	def jsonArray:Option[JSONArray]		= downcast(JSONSubtypes.jsonArray)
	def jsonObject:Option[JSONObject]	= downcast(JSONSubtypes.jsonObject)
	
	//------------------------------------------------------------------------------
	
	def nullRef:Option[Null]						= extract(JSONSubtypes.jsonNull,	JSONBijections.jsonNull)
	def string:Option[String]						= extract(JSONSubtypes.jsonString,	JSONBijections.jsonString)
	def decimal:Option[BigDecimal]					= extract(JSONSubtypes.jsonNumber,	JSONBijections.jsonNumber)
	def long:Option[Long]							= decimal map { _.longValue		}
	def int:Option[Int]								= decimal map { _.intValue		}
	def double:Option[Double]						= decimal map { _.doubleValue	}
	def float:Option[Float]							= decimal map { _.floatValue	}
	def boolean:Option[Boolean]						= extract(JSONSubtypes.jsonBoolean,	JSONBijections.jsonBoolean)
	def arraySeq:Option[ISeq[JSONValue]]			= extract(JSONSubtypes.jsonArray,	JSONBijections.jsonArray)
	def objectSeq:Option[ISeq[(String,JSONValue)]]	= extract(JSONSubtypes.jsonObject,	JSONBijections.jsonObject)
	def objectMap:Option[Map[String,JSONValue]]		= objectSeq map { _.toMap }
}
