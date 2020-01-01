package scjson.converter

import scjson.converter.{
	ApplyUnapplyConverters	=> AU
}

trait NewtypeJsonReaders {
	def newtypeReader[S,T1:JsonReader](apply:(T1)=>S):JsonReader[S]	=
		JsonReader[T1] >=> (AU apply1 apply)
}
