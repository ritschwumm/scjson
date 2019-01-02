package scjson.converter

import scutil.base.implicits._
import scutil.lang._

import scjson.converter.{
	CollectionConverters	=> CC,
	JsonConverters			=> JC,
	KeyConverters			=> KC
}

trait CollectionJsonReaders extends CollectionJsonReadersLow {
	implicit def VectorReader[T:JsonReader]:JsonReader[Vector[T]]	=
			JC.expectArray					>=>
			(Converter total (_.toVector))	>=>
			JsonReader[T].liftVector

	implicit def SetReader[T:JsonReader]:JsonReader[Set[T]]	=
			JsonReader[ISeq[T]] map { _.toSet }

	implicit def NesReader[T:JsonReader]:JsonReader[Nes[T]]	=
			VectorReader	>=>
			(Converter optional (_.toNesOption, JsonError("expected at least 1 element")))

	implicit def KeyMapReader[K:JsonKeyReader,V:JsonReader]:JsonReader[Map[K,V]]	=
			JC.expectObject		>=>
			(KC.StringToKey >=> JsonKeyReader[K] pair JsonReader[V]).liftISeq	>=>
			CC.pairsToMap
}

trait CollectionJsonReadersLow {
	implicit def ISeqReader[T:JsonReader]:JsonReader[ISeq[T]]	=
			JC.expectArray >=>
			JsonReader[T].liftISeq
}

/*
trait CollectionJsonReadersLow extends TupleJsonReaders {
	implicit def MapReader[K:JsonReader,V:JsonReader]:JsonReader[Map[K,V]]	=
			JsonReader[ISeq[(K,V)]] map { _.toMap }

	implicit def MapReader[K:JsonReader,V:JsonReader]:JsonReader[Map[K,V]]	=
			JC.expectArray				>=>
			JsonReader[(K,V)].liftISeq	map
			(_.toMap)

	//------------------------------------------------------------------------------

	implicit def StringMapReader[T:JsonReader]:JsonReader[Map[String,T]]	=
			specialMapReader(Converter.identity)

	implicit def IntMapReader[T:JsonReader]:JsonReader[Map[Int,T]]	=
			specialMapReader(NS.StringToInt)

	implicit def LongMapReader[T:JsonReader]:JsonReader[Map[Long,T]]	=
			specialMapReader(NS.StringToLong)

	implicit def BigIntMapReader[T:JsonReader]:JsonReader[Map[BigInt,T]]	=
			specialMapReader(NS.StringToBigInt)

	protected def specialMapReader[S,T:JsonReader](key:JsonConverter[String,S]):JsonReader[Map[S,T]]	=
			JC.expectObject						>=>
			(key pair JsonReader[T]).liftISeq	>=>
			CC.pairsToMap
}
*/


