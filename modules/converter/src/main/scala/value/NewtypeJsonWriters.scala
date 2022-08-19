package scjson.converter

import scala.deriving.Mirror

import scutil.lang.Bijection

trait NewtypeJsonWriters {
	@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
	def newtypeWriter[S<:Product](using m:Mirror.ProductOf[S], ev: m.MirroredElemTypes <:< Tuple1[?], f:JsonWriter[Tuple.Elem[m.MirroredElemTypes, 0]]):JsonWriter[S]	= {
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

	def bijectionWriter[S,T:JsonWriter](bijection:Bijection[S,T]):JsonWriter[S]	=
		JsonWriter[T] contraMap bijection.get
}
