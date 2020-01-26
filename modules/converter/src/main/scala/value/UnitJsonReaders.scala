package scjson.converter

import scutil.lang._

import scjson.converter.{ JsonConverters => JC }

trait UnitJsonReaders {
	// use an empty JsonObject, because JsonNull is used for None
	implicit val UnitReader:JsonReader[Unit]	=
		JC.expectObject >=>
		(Converter { it =>
			if (it.isEmpty)	Validated good	 (())
			else			Validated bad	JsonError("expected an empty object")
		})
}
