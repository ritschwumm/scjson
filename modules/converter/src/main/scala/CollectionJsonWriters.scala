package scjson.converter

import scutil.lang._

import scjson.converter.{
	CollectionConverters	=> CC,
	JsonConverters			=> JC,
	KeyConverters			=> KC
}

trait CollectionJsonWriters extends CollectionJsonWritersLow {
	implicit def VectorWriter[T:JsonWriter]:JsonWriter[Vector[T]]	=
		JsonWriter[T].liftVector	>=>
		JC.makeArray.varyIn

	implicit def SetWriter[T:JsonWriter]:JsonWriter[Set[T]]	=
		JsonWriter[Seq[T]] contraMap { _.toVector }

	implicit def NesWriter[T:JsonWriter]:JsonWriter[Nes[T]]	=
		(Converter total ((_:Nes[T]).toVector))	>=>
		VectorWriter

	implicit def KeyMapWriter[K:JsonKeyWriter,V:JsonWriter]:JsonWriter[Map[K,V]]	=
		CC.mapToPairs		>=>
		(JsonKeyWriter[K] >=> KC.KeyToString pair JsonWriter[V]).liftSeq	>=>
		JC.makeObject
}

trait CollectionJsonWritersLow {
	implicit def SeqWriter[T:JsonWriter]:JsonWriter[Seq[T]]	=
		JsonWriter[T].liftSeq	>=>
		JC.makeArray

	private object MyTupleJsonWriters extends TupleJsonWriters
	implicit def KeylessMapWriter[K:JsonWriter,V:JsonWriter]:JsonWriter[Map[K,V]]	=
			SeqWriter(MyTupleJsonWriters.Tuple2Writer[K,V]) contraMap (_.toVector)
}
