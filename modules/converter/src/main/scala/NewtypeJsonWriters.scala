package scjson.converter

import scjson.converter.{
	ApplyUnapplyConverters	=> AU
}

trait NewtypeJsonWriters {
	def newtypeWriter[S,T1:JsonWriter](unapply:S=>Option[T1]):JsonWriter[S]	=
		(AU unapply1 unapply)	>=> JsonWriter[T1]

	def newtypeWriterTotal[S,T1:JsonWriter](unapply:S=>T1):JsonWriter[S]	=
		JsonWriter[T1] contraMap unapply
}
