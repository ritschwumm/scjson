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
			JsonWriter[ISeq[T]] contraMap { _.toVector }

	implicit def NesWriter[T:JsonWriter]:JsonWriter[Nes[T]]	=
			(Converter total ((_:Nes[T]).toVector))	>=>
			VectorWriter

	implicit def KeyMapWriter[K:JsonKeyWriter,V:JsonWriter]:JsonWriter[Map[K,V]]	=
			CC.mapToPairs		>=>
			(JsonKeyWriter[K] >=> KC.KeyToString pair JsonWriter[V]).liftISeq	>=>
			JC.makeObject
}

trait CollectionJsonWritersLow {
	implicit def ISeqWriter[T:JsonWriter]:JsonWriter[ISeq[T]]	=
			JsonWriter[T].liftISeq	>=>
			JC.makeArray

	private object MyTupleJsonWriters extends TupleJsonWriters
	implicit def KeylessMapWriter[K:JsonWriter,V:JsonWriter]:JsonWriter[Map[K,V]]	=
			ISeqWriter(MyTupleJsonWriters.Tuple2Writer[K,V]) contraMap (_.toVector)
}
