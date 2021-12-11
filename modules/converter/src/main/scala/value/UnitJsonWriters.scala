package scjson.converter

trait UnitJsonWriters {
	given UnitWriter:JsonWriter[Unit]	= UnitConverters.UnitWriter
}
