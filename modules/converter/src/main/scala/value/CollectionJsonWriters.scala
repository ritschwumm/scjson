package scjson.converter

import scutil.lang.*

import scjson.converter.{
	CollectionConverters	as CC,
	JsonConverters			as JC,
	KeyConverters			as KC
}

trait CollectionJsonWriters extends CollectionJsonWritersLow {
	given VectorWriter[T:JsonWriter]:JsonWriter[Vector[T]]	=
		JsonWriter[T].liftVector	>=>
		JC.makeArray.varyIn

	given SetWriter[T:JsonWriter]:JsonWriter[Set[T]]	=
		JsonWriter[Seq[T]].contraMap(_.toVector)

	given NesWriter[T:JsonWriter]:JsonWriter[Nes[T]]	=
		Converter.total((_:Nes[T]).toVector)	>=>
		VectorWriter

	given KeyMapWriter[K:JsonKeyWriter,V:JsonWriter]:JsonWriter[Map[K,V]]	=
		CC.mapToPairs		>=>
		(JsonKeyWriter[K] >=> KC.KeyToString).pair(JsonWriter[V]).liftSeq	>=>
		JC.makeObject
}

trait CollectionJsonWritersLow {
	given SeqWriter[T:JsonWriter]:JsonWriter[Seq[T]]	=
		JsonWriter[T].liftSeq	>=>
		JC.makeArray

	private object MyTupleJsonWriters extends TupleJsonWriters

	given KeylessMapWriter[K:JsonWriter,V:JsonWriter]:JsonWriter[Map[K,V]]	=
		SeqWriter(using MyTupleJsonWriters.Tuple2Writer[K,V]).contraMap(_.toVector)
}
