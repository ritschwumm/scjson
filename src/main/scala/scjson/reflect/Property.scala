package scjson.reflect

object Property {
	def fromMangled(mangled:String):Property	= Property(Mangling unmangle mangled, mangled)
	def fromPlain(plain:String):Property		= Property(plain, Mangling mangle plain)
}

case class Property(plain:String, mangled:String)
