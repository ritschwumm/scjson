package scjson

import java.io.File

final class JSONFileException(
	file:File, cause:Exception=null
)
extends JSONInputException(
	"cannot read file " + file, cause
)
