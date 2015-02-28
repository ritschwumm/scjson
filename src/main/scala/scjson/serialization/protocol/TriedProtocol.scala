package scjson.serialization

import scutil.lang._

import scjson._

import JSONSerializationUtil._

object TriedProtocol extends TriedProtocol

trait TriedProtocol {
	private val winTag	= "win"
	private val failTag	= "fail"
		
	implicit def TriedFormat[F:Format,W:Format]:Format[Tried[F,W]]	=
			Format[Tried[F,W]](
				_ match {
					case Fail(value)	=> JSONVarObject(
						failTag	-> doWrite[F](value)
					)
					case Win(value)		=> JSONVarObject(
						winTag	-> doWrite[W](value)
					)
				},
				(in:JSONValue)	=> {
					val	map	= objectMap(in)
					(map get failTag, map get winTag) match {
						case (Some(bs), None)	=> Fail(doRead[F](bs))
						case (None, Some(bs))	=> Win(doRead[W](bs))
						case _					=> fail("unexpected tried")
					}
				}
			)
}
