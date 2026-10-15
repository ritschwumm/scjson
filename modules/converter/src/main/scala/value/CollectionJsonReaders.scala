package scjson.converter

import scutil.core.implicits.*
import scutil.lang.*

trait CollectionJsonReaders extends CollectionJsonReadersLow {
	given VectorReader: [T:JsonReader] => JsonReader[Vector[T]]	=
		JsonConverters.expectArray	>=>
		Converter.total(_.toVector)	>=>
		JsonReader[T].liftVector

	given SetReader: [T:JsonReader] => JsonReader[Set[T]]	=
		JsonReader[Seq[T]].map(_.toSet)

	given NesReader: [T:JsonReader] => JsonReader[Nes[T]]	=
		VectorReader	>=>
		Converter.optional(_.toNesOption, JsonError("expected at least 1 element"))

	given KeyMapReader: [K:JsonKeyReader,V:JsonReader] => JsonReader[Map[K,V]]	=
		JsonConverters.expectObject	>=>
		(KeyConverters.StringToKey >=> JsonKeyReader[K]).pair(JsonReader[V]).liftSeq	>=>
		CollectionConverters.pairsToMap
}

trait CollectionJsonReadersLow {
	given SeqReader: [T:JsonReader] => JsonReader[Seq[T]]	=
		JsonConverters.expectArray >=>
		JsonReader[T].liftSeq

	private object MyTupleJsonReaders extends TupleJsonReaders

	given KeylessMapReader: [K:JsonReader,V:JsonReader] => JsonReader[Map[K,V]]	=
		SeqReader(using MyTupleJsonReaders.Tuple2Reader[K,V]).map(_.toMap)
}
