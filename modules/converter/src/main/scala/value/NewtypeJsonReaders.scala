package scjson.converter

import scala.deriving.Mirror

import scutil.lang.Bijection

trait NewtypeJsonReaders {
	def newtypeReader[S](using m:Mirror.ProductOf[S], ev: m.MirroredElemTypes <:< Tuple1[?], f:JsonReader[Tuple.Elem[m.MirroredElemTypes, 0]]):JsonReader[S]	=
		f.map { t => m.fromProduct(t *: EmptyTuple) }

	def bijectionReader[S:JsonReader,T](bijection:Bijection[T,S]):JsonReader[T]	=
		JsonReader[S] map bijection.set
}
