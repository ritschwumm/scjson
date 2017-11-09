package scjson.pickle.protocol

import scjson.ast._
import scjson.pickle._

import JSONPickleUtil._

object NullOptionProtocol extends NullOptionProtocol

trait LowPrioNullOptionProtocol {
	// alternative value or null
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
			Format[Option[T]](
				_ match {
					case None			=> JSONNull
					case Some(value)	=> doWrite(value)
				},
				_ match {
					case JSONNull	=> None
					case js			=> Some(doReadUnsafe[T](js))
				}
			)
}

trait NullOptionProtocol extends LowPrioNullOptionProtocol {
	private val someTag	= "some"
	private val noneTag	= "none"
	
	// alternative {some} or {none}
	implicit def OptionOptionFormat[T](implicit ev:Format[Option[T]]):Format[Option[Option[T]]]	=
			Format[Option[Option[T]]](
				_ match {
					case Some(value)	=> JSONObject.Var(someTag -> doWrite(value))
					case None			=> JSONObject.Var(noneTag -> JSONTrue)
				},
				(in:JSONValue)	=> {
					val map	= objectMap(in)
					(map get someTag, map get noneTag) match {
						case (Some(js), None)	=> Some(doReadUnsafe[Option[T]](js))
						case (None, Some(js))	=> None
						case _					=> fail("unexpected option")
					}
				}
			)
}
