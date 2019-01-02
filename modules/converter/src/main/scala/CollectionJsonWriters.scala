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
}

/*
trait CollectionJsonWritersLow extends TupleJsonWriters {
	implicit def MapWriter[K:JsonWriter,V:JsonWriter]:JsonWriter[Map[K,V]]	=
			JsonWriter[ISeq[(K,V)]] contraMap { _.toVector }

	implicit def MapWriter[K:JsonWriter,V:JsonWriter]:JsonWriter[Map[K,V]]	=
			JsonWriter[(K,V)].liftISeq	>=>
			JC.makeArray				contraMap
			{ _.toVector }

	//------------------------------------------------------------------------------

	implicit def StringMapWriter[T:JsonWriter]:JsonWriter[Map[String,T]]	=
			specialMapWriter(Converter.identity)

	implicit def IntMapWriter[T:JsonWriter]:JsonWriter[Map[Int,T]]	=
			specialMapWriter(NS.IntToString)

	implicit def LongMapWriter[T:JsonWriter]:JsonWriter[Map[Long,T]]	=
			specialMapWriter(NS.LongToString)

	implicit def BigIntMapWriter[T:JsonWriter]:JsonWriter[Map[BigInt,T]]	=
			specialMapWriter(NS.BigIntToString)

	protected def specialMapWriter[S,T:JsonWriter](key:JsonConverter[S,String]):JsonWriter[Map[S,T]]	=
			CC.mapToPairs[JsonError,S,T]		>=>
			(key pair JsonWriter[T]).liftISeq	>=>
			JC.makeObject
}
*/
