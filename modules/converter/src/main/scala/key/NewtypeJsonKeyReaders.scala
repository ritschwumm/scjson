package scjson.converter

import scala.deriving.Mirror

import scutil.lang.Bijection

trait NewtypeJsonKeyReaders {
	def newtypeKeyReader[S](using m:Mirror.ProductOf[S], ev: m.MirroredElemTypes <:< Tuple1[?], f:JsonKeyReader[Tuple.Elem[m.MirroredElemTypes, 0]]):JsonKeyReader[S]	=
		f.map { t => m.fromProduct(t *: EmptyTuple) }

	def bijectionKeyReader[S:JsonKeyReader,T](bijection:Bijection[T,S]):JsonKeyReader[T]	=
		JsonKeyReader[S].map(bijection.set)
}
