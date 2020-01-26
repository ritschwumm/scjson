package scjson.converter

import scutil.base.implicits._
import scutil.lang._
import scjson.ast._
import scjson.converter.{ JsonConverters => JC }

// BETTER json rework
object SumConverters {
	val expectTagged:JsonConverter[JsonValue,(String,JsonValue)]	=
		JC.expectObject >=>
		Converter { it =>
			it.singleOption toGood JsonError(show"expected exactly one element, found ${it.size}")
		}

	val makeTagged:JsonConverter[(String,JsonValue),JsonValue]	=
		(Converter total { kv:(String,JsonValue) => Seq(kv) })	>=>
		JC.makeObject

	//------------------------------------------------------------------------------

	def sumReaderVar[T](partials:(String,JsonReader[T])*):JsonReader[T]	=
		sumReader(partials)

	def sumWriterVar[T](partials:(String,JsonConverter[T,Option[JsonValue]])*):JsonWriter[T]	=
		sumWriter(partials)

	def sumReader[T](partials:Seq[(String,JsonReader[T])]):JsonReader[T]	=
		expectTagged >=>
		Converter { case (k, v) =>
			partials
			.collectFirst	{ case (`k`, conv) => conv convert v }
			.getOrElse		(JsonBad(show"unexpected key $k"))
		}

	def sumWriter[T](partials:Seq[(String,JsonConverter[T,Option[JsonValue]])]):JsonWriter[T]	=
		(Converter { (it:T) =>
			val a:JsonResult[Seq[Option[(String,JsonValue)]]]	=
				partials traverseValidated { case (k, conv) =>
					conv convert it map { _ map ((k, _)) }
				}

			val b:JsonResult[Seq[(String,JsonValue)]]	=
				a map { _.collapse }

			val c:JsonResult[(String,JsonValue)]	=
				b flatMap { pairs =>
					pairs.singleEither
					.leftMap {
						case false	=> JsonError(show"expected unique ctor, found none")
						case true	=> JsonError(show"expected unique ctor, found multiple")
					}
					.toValidated
				}

			c
		}) >=>
		makeTagged

	//------------------------------------------------------------------------------

	def subReader[Super,Sub<:Super](reader:JsonReader[Sub]):JsonReader[Super]	=
		reader.varyOut
		//Converter(reader.convert)

	def subWriter[Super,Sub<:Super](writer:JsonWriter[Sub], ctor:PFunction[Super,Sub]):JsonConverter[Super,Option[JsonValue]]	=
		Converter(ctor(_) traverseValidated writer.convert)

	//------------------------------------------------------------------------------

	def prismReader[E,R,S,T](prism:Prism[S,T], reader:Converter[E,R,T]):Converter[E,R,S]	=
		reader map prism.set

	def prismWriter[E,S,T,U](prism:Prism[S,T], writer:Converter[E,T,U]):Converter[E,S,Option[U]]	=
		Converter {	s =>
			prism get s traverseValidated writer.convert
		}

	/*
	implicit class PrismConverterExt[S,T](peer:Prism[S,T]) {
		def subReaderOf[E,R](reader:Converter[E,R,T]):Converter[E,R,S]			= prismReader(peer, reader)
		def subWriterOf[E,U](writer:Converter[E,T,U]):Converter[E,S,Option[U]]	= prismWriter(peer, writer)
	}
	*/
}
