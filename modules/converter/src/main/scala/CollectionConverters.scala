package scjson.converter

import scutil.lang.implicits._
import scutil.lang._

object CollectionConverters {
	def mapToPairs[E,K,V]:Converter[E,Map[K,V],Seq[(K,V)]]	=
			Converter total (_.toVector)

	def pairsToMap[E,K,V]:Converter[E,Seq[(K,V)],Map[K,V]]	=
			Converter total (_.toMap)

	//------------------------------------------------------------------------------

	def pairToMap[E,K,V](k1:K, k2:K):Converter[E,(V,V),Map[K,V]]	=
			Converter total { values	=>
				Map(
					k1 -> values._1,
					k2 -> values._2
				)
			}

	@SuppressWarnings(Array("org.wartremover.warts.ToString"))
	def mapToPair[K,V](k1:K, k2:K):JsonConverter[Map[K,V],(V,V)]	=
			Converter { (it:Map[K,V]) =>
				if (it.size == 2) {
					(it get k1 toGood JsonError(show"missing element ${k1.toString}"))	zip
					(it get k2 toGood JsonError(show"missing element ${k2.toString}"))
				}
				else {
					Validated bad JsonError(show"expected 2 values, got ${it.size}")
				}
			}

	//------------------------------------------------------------------------------

	def eitherToMap[E,K,V](k1:K, k2:K):Converter[E,Either[V,V],Map[K,V]]	=
			Converter total {
				case Left(x)	=> Map(k1 -> x)
				case Right(x)	=> Map(k2 -> x)
			}

	@SuppressWarnings(Array("org.wartremover.warts.ToString"))
	def mapToEither[K,V](k1:K, k2:K):JsonConverter[Map[K,V],Either[V,V]]	=
			Converter { (it:Map[K,V]) =>
				if (it.size == 1) {
					(it get k1 map { v => Validated good[JsonError,Either[V,V]] (Left(v) :Either[V,V]) })	orElse
					(it get k2 map { v => Validated good[JsonError,Either[V,V]] (Right(v):Either[V,V]) })	getOrElse
					(Validated bad JsonError(show"missing element ${k1.toString} or ${k2.toString}"))
				}
				else {
					Validated bad JsonError(show"expected 1 value, got ${it.size}")
				}
			}
}
