package scjson.converter

import scjson.converter.{
	ApplyUnapplyConverters	=> AU
}

trait NewtypeJsonKeyWriters {
	def newtypeKeyWriter[S,T1:JsonKeyWriter](unapply:S=>Option[T1]):JsonKeyWriter[S]	=
		(AU unapply1 unapply)	>=> JsonKeyWriter[T1]
}
