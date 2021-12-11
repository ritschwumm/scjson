package scjson.converter

trait UnitJsonReaders {
	given UnitReader:JsonReader[Unit]	= UnitConverters.UnitReader
}
