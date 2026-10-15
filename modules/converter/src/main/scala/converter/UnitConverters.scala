package scjson.converter

import scutil.lang.*
import scjson.ast.*

object UnitConverters {
	/** uses an empty json object, because json null is used for None - and because we have an object then we can put an (oldskool) type tag into */
	val UnitReader:JsonReader[Unit]	=
		JsonConverters.expectObject >=>
		(Converter { it =>
			if (it.isEmpty)	Validated.valid(())
			else			JsonInvalid("expected an empty object")
		})

	/** uses an empty json object, because json null is used for None - and because we have an object then we can put an (oldskool) type tag into */
	val UnitWriter:JsonWriter[Unit]	=
		Converter.constant(JsonValue.emptyObject)
}
