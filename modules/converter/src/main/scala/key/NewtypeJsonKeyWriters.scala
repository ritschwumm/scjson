package scjson.converter

import scala.deriving.Mirror

import scutil.lang.Bijection

trait NewtypeJsonKeyWriters {
	@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
	def newtypeKeyWriter[S<:Product](using m:Mirror.ProductOf[S], ev: m.MirroredElemTypes <:< Tuple1[?], f:JsonKeyWriter[Tuple.Elem[m.MirroredElemTypes, 0]]):JsonKeyWriter[S]	= {
		// TODO this sucks, but scala is not smart enough to find out
		// i'm manually doing a Map[MirroredElemTypes,JsonWriter]
		// and this type is identical to m.MirroredElemTypes
		type Values	=
			Tuple1[
				Tuple.Elem[m.MirroredElemTypes, 0]
			]

		f.contraMap { s =>
			Tuple.fromProductTyped(s).asInstanceOf[Values](0)
		}
	}

	def bijectionKeyWriter[S,T:JsonKeyWriter](bijection:Bijection[S,T]):JsonKeyWriter[S]	=
		JsonKeyWriter[T].contraMap(bijection.get)
}
