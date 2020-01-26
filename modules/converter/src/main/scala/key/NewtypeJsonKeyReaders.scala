package scjson.converter

import scutil.lang.Bijection

import scjson.converter.{
	ApplyUnapplyConverters	=> AU
}

trait NewtypeJsonKeyReaders {
	def newtypeKeyReader[S,T1:JsonKeyReader](apply:(T1)=>S):JsonKeyReader[S]	=
		JsonKeyReader[T1] >=> (AU apply1 apply)

	def bijectionKeyReader[S:JsonKeyReader,T](bijection:Bijection[T,S]):JsonKeyReader[T]	=
		JsonKeyReader[S] map bijection.set
}
