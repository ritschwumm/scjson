package scjson.converter

trait UnitJsonWriters {
	implicit val UnitWriter:JsonWriter[Unit]	= UnitConverters.UnitWriter
}
