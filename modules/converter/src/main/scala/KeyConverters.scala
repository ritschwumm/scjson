package scjson.converter

import scutil.lang._

object KeyConverters {
	val StringToKey:JsonConverter[String,JsonKey]	= Converter total JsonKey.apply
	val KeyToString:JsonConverter[JsonKey,String]	= Converter total (_.value)
}
