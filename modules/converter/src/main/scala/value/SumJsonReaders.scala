package scjson.converter

import scutil.core.implicits.*
import scutil.lang.*

import scjson.converter.{
	SumConverters	as SC
}

// BETTER json rework
trait SumJsonReaders {
	def sumReaderVar[T](partials:(String,JsonReader[T])*):JsonReader[T]	=
		sumReader(partials)

	def sumReader[T](partials:Seq[(String,JsonReader[T])]):JsonReader[T]	=
		SC.expectTagged >=>
		Converter { case (k, v) =>
			partials
			.collectFirst	{ case (`k`, conv) => conv convert v }
			.getOrElse		(JsonInvalid(show"unexpected key '$k'"))
		}

	def subReader[Super,Sub<:Super](reader:JsonReader[Sub]):JsonReader[Super]	=
		reader.varyOut
		//Converter(reader.convert)

	def prismReader[E,R,S,T](prism:Prism[S,T], reader:Converter[E,R,T]):Converter[E,R,S]	=
		reader map prism.set
}
