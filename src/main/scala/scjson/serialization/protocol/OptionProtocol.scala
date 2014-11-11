package scjson.serialization

import scjson._

import JSONSerializationUtil._

object OptionProtocol extends OptionProtocol

trait OptionProtocol {
	private val someTag	= "some"
	private val noneTag	= "none"
		
	/*
	// alternative 0/1-sized array
	implicit def OptionFormat1[T:Format]:Format[Option[T]]	=
			Format[Option[T]](
				(out:Option[T])	=> doWrite[ISeq[T]](out.toSeq),
				(in:JSONValue)	=> doRead[ISeq[T]](in) match {
					case ISeq(t)	=> Some(t)
					case ISeq()		=> None
					case _			=> fail("expected 0 or 1 elements for an Option")
				}
			)
	*/
	
	// alternative {some} or {none}
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
			Format[Option[T]](
				_ match {
					case Some(value)	=> JSONVarObject(someTag -> doWrite(value))
					case None			=> JSONVarObject(noneTag -> JSONTrue)
				},
				(in:JSONValue)	=> {
					val map	= objectMap(in)
					(map get someTag, map get noneTag) match {
						case (Some(js), None)	=> Some(doRead[T](js))
						case (None, Some(js))	=> None
						case _					=> fail("unexpected option")
					}
				}
			)
}
