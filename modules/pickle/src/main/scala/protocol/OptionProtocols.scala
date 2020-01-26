package scjson.pickle.protocol

import scjson.ast._
import scjson.pickle._

import JsonPickleUtil._

object OptionProtocols {
	private val someTag	= "some"
	private val noneTag	= "none"

	/*
	// alternative 0/1-sized array
	def arrayFormat[T:Format]:Format[Option[T]]	=
		Format[Option[T]](
			(out:Option[T])	=> doWrite[Seq[T]](out.toSeq),
			(in:JsonValue)	=> doRead[Seq[T]](in) match {
				case Seq(t)	=> Some(t)
				case Seq()	=> None
				case _		=> fail("expected 0 or 1 elements for an Option")
			}
		)
	*/

	// alternative {some} or {none}
	def adtFormat[T:Format]:Format[Option[T]]	=
		Format[Option[T]](
			_ match {
				case Some(value)	=> JsonObject.Var(someTag -> doWrite(value))
				case None			=> JsonObject.Var(noneTag -> JsonTrue)
			},
			(in:JsonValue)	=> {
				val map	= objectMap(in)
				(map get someTag, map get noneTag) match {
					case (Some(js),	None)		=> Some(doReadUnsafe[T](js))
					case (None,		Some(js))	=> None
					case _						=> fail("unexpected option")
				}
			}
		)

	// alternative value or null
	// NOTE this must not be used for an Option that contains another Option!
	def nullFormat[T:Format]:Format[Option[T]]	=
		Format[Option[T]](
			_ match {
				case None			=> JsonNull
				case Some(value)	=> doWrite(value)
			},
			_ match {
				case JsonNull	=> None
				case js			=> Some(doReadUnsafe[T](js))
			}
		)
}
