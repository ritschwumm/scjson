package scjson.converter

import scutil.lang.Bijection

import scjson.converter.{
	ApplyUnapplyConverters	=> AU
}

trait NewtypeJsonKeyWriters {
	def newtypeKeyWriter[S,T1:JsonKeyWriter](unapply:S=>Option[T1]):JsonKeyWriter[S]	=
		(AU unapply1 unapply)	>=> JsonKeyWriter[T1]

	def newtypeKeyWriterTotal[S,T1:JsonKeyWriter](unapply:S=>T1):JsonKeyWriter[S]	=
		JsonKeyWriter[T1] contraMap unapply

	def bijectionKeyWriter[S,T:JsonKeyWriter](bijection:Bijection[S,T]):JsonKeyWriter[S]	=
		JsonKeyWriter[T] contraMap bijection.get
}
