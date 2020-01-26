package scjson.converter

trait UnitJsonReaders {
	implicit val UnitReader:JsonReader[Unit]	= UnitConverters.UnitReader
}
