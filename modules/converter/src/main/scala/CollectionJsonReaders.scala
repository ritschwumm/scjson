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
			JsonReader[Seq[T]] map { _.toSet }

	implicit def NesReader[T:JsonReader]:JsonReader[Nes[T]]	=
			VectorReader	>=>
			(Converter optional (_.toNesOption, JsonError("expected at least 1 element")))

	implicit def KeyMapReader[K:JsonKeyReader,V:JsonReader]:JsonReader[Map[K,V]]	=
			JC.expectObject		>=>
			(KC.StringToKey >=> JsonKeyReader[K] pair JsonReader[V]).liftSeq	>=>
			CC.pairsToMap
}

trait CollectionJsonReadersLow {
	implicit def SeqReader[T:JsonReader]:JsonReader[Seq[T]]	=
			JC.expectArray >=>
			JsonReader[T].liftSeq

	private object MyTupleJsonReaders extends TupleJsonReaders
	implicit def KeylessMapReader[K:JsonReader,V:JsonReader]:JsonReader[Map[K,V]]	=
			SeqReader(MyTupleJsonReaders.Tuple2Reader[K,V]) map (_.toMap)
}
