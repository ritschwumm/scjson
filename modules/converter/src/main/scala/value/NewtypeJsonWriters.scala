package scjson.converter

import scutil.lang.Bijection

import scjson.converter.{
	ApplyUnapplyConverters	=> AU
}

trait NewtypeJsonWriters {
	def newtypeWriter[S,T1:JsonWriter](unapply:S=>Option[T1]):JsonWriter[S]	=
		(AU unapply1 unapply)	>=> JsonWriter[T1]

	def newtypeWriterTotal[S,T1:JsonWriter](unapply:S=>T1):JsonWriter[S]	=
		JsonWriter[T1] contraMap unapply

	def bijectionWriter[S,T:JsonWriter](bijection:Bijection[S,T]):JsonWriter[S]	=
		JsonWriter[T] contraMap bijection.get
}
