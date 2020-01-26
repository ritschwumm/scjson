package scjson.converter

import scutil.lang._
import scjson.ast._

import scjson.converter.{ JsonConverters => JC }

object UnitConverters {
	/** uses an empty JsonObject, because JsonNull is used for None - and because we have an object then we can put an (oldskool) type tag into */
	val UnitReader:JsonReader[Unit]	=
		JC.expectObject >=>
		(Converter { it =>
			if (it.isEmpty)	Validated good	 (())
			else			JsonBad("expected an empty object")
		})

	/** uses an empty JsonObject, because JsonNull is used for None - and because we have an object then we can put an (oldskool) type tag into */
	val UnitWriter:JsonWriter[Unit]	=
		Converter constant JsonObject.empty
}
