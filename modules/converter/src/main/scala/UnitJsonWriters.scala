package scjson.converter

import scutil.lang._
import scjson.ast._

trait UnitJsonWriters {
	// use an empty JsonObject, because JsonNull is used for None
	implicit val UnitWriter:JsonWriter[Unit]	=
			Converter constant JsonObject.empty
}
