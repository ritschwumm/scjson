package scjson.io

import java.io._

import scjson.codec._
import scjson.pickle._

sealed trait JsonIoStringFailure
sealed trait JsonIoFileFailure

final case class JsonIoExceptionFailure(cause:IOException)			extends JsonIoFileFailure
final case class JsonIoDecodeFailure(base:JsonDecodeFailure)		extends JsonIoFileFailure with JsonIoStringFailure 
final case class JsonIoUnpickleFailure(base:JsonUnpickleFailure)	extends JsonIoFileFailure with JsonIoStringFailure
