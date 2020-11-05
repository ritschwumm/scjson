package scjson.converter

import scutil.core.implicits._
import scutil.lang._

import scjson.ast._
import scjson.converter.{
	SumConverters	=> SC
}

// BETTER json rework
trait SumJsonWriters {
	def sumWriterVar[T](partials:(String,JsonConverter[T,Option[JsonValue]])*):JsonWriter[T]	=
		sumWriter(partials)

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
		SC.makeTagged

	def subWriter[Super,Sub<:Super](writer:JsonWriter[Sub], ctor:Super=>Option[Sub]):JsonConverter[Super,Option[JsonValue]]	=
		Converter(ctor(_) traverseValidated writer.convert)

	def prismWriter[E,S,T,U](prism:Prism[S,T], writer:Converter[E,T,U]):Converter[E,S,Option[U]]	=
		Converter {	s =>
			prism get s traverseValidated writer.convert
		}
}
