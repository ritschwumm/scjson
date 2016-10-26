package scjson.io

import java.io._

import scjson.codec._
import scjson.pickle._

sealed trait JSONIOStringFailure
sealed trait JSONIOFileFailure

final case class JSONIOExceptionFailure(cause:IOException)			extends JSONIOFileFailure
final case class JSONIODecodeFailure(base:JSONDecodeFailure)		extends JSONIOFileFailure with JSONIOStringFailure 
final case class JSONIOUnpickleFailure(base:JSONUnpickleFailure)	extends JSONIOFileFailure with JSONIOStringFailure
