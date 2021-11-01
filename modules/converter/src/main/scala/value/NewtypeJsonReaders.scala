package scjson.converter

import scutil.lang.Bijection

import scjson.converter.{
	ApplyUnapplyConverters	=> AU
}

trait NewtypeJsonReaders {
	def newtypeReader[S,T1:JsonReader](apply:(T1)=>S):JsonReader[S]	=
		JsonReader[T1] >=> AU.applyNewtype(apply)

	def bijectionReader[S:JsonReader,T](bijection:Bijection[T,S]):JsonReader[T]	=
		JsonReader[S] map bijection.set
}
