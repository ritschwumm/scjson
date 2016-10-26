package scjson.pickle.protocol

import scutil.lang._

import scjson.ast._
import scjson.pickle._

import JSONPickleUtil._

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
						case (Some(bs), None)	=> Fail(doReadUnsafe[F](bs))
						case (None, Some(bs))	=> Win(doReadUnsafe[W](bs))
						case _					=> fail("unexpected tried")
					}
				}
			)
}
