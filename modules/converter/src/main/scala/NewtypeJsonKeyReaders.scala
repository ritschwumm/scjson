package scjson.converter

import scjson.converter.{
	ApplyUnapplyConverters	=> AU
}

trait NewtypeJsonKeyReaders {
	def newtypeKeyReader[S,T1:JsonKeyReader](apply:(T1)=>S):JsonKeyReader[S]	=
			JsonKeyReader[T1] >=> (AU apply1 apply)
}
