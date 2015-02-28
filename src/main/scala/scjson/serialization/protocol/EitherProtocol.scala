package scjson.serialization

import scjson._

import JSONSerializationUtil._

object EitherProtocol extends EitherProtocol

trait EitherProtocol {
	private val rightTag	= "right"
	private val leftTag		= "left"
		
	// alternative {left} or {right}
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	=
			Format[Either[L,R]](
				_ match {
					case Right(value)	=> JSONVarObject(
						rightTag	-> doWrite[R](value)
					)
					case Left(value)	=> JSONVarObject(
						leftTag		-> doWrite[L](value)
					)
				},
				(in:JSONValue)	=> {
					val	map	= objectMap(in)
					(map get leftTag, map get rightTag) match {
						case (None, Some(js))	=> Right(doRead[R](js))
						case (Some(js), None)	=> Left(doRead[L](js))
						case _					=> fail("unexpected either")
					}
				}
			)
}
