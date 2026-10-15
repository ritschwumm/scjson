package scjson.converter

object AltSeqConverters {
	def seqReader[T1:JsonReader,T2:JsonReader](first:String, second:String):JsonReader[(T1,T2)]	=
		JsonConverters.expectObjectMap			>=>
		CollectionConverters.mapToPair(first, second)	>=>
		JsonReader[T1].pair(JsonReader[T2])

	def seqWriter[T1:JsonWriter,T2:JsonWriter](first:String, second:String):JsonWriter[(T1,T2)]	=
		JsonWriter[T1].pair(JsonWriter[T2])	>=>
		CollectionConverters.pairToMap(first, second)			>=>
		JsonConverters.makeObjectMap

	def altReader[T1:JsonReader,T2:JsonReader](left:String, right:String):JsonReader[Either[T1,T2]]	=
		JsonConverters.expectObjectMap			>=>
		CollectionConverters.mapToEither(left, right)	>=>
		JsonReader[T1].either(JsonReader[T2])

	def altWriter[T1:JsonWriter,T2:JsonWriter](left:String, right:String):JsonWriter[Either[T1,T2]]	=
		JsonWriter[T1].either(JsonWriter[T2])	>=>
		CollectionConverters.eitherToMap(left, right)				>=>
		JsonConverters.makeObjectMap
}
