package scjson

/** base class for json reading problems */
abstract class JSONInputException(
	message:String, cause:Exception=null
)
extends Exception(
	message, cause
)
