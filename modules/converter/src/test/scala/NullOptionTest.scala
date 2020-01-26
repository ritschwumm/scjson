package scjson.converter

import org.specs2.mutable._

import scjson.ast._

class NullOptionTest extends Specification {
	import JsonFormat._

	"plain option" should {
		"serialize None as null" in {
			writeAst(None:Option[String]) mustEqual Right(JsonNull)
		}
		"serialize Some as value" in {
			writeAst(Some("hallo"):Option[String]) mustEqual Right(JsonString("hallo"))
		}
	}

	"nested option" should {
		"serialize None as {none:{}}" in {
			writeAst(None:Option[Option[String]]) mustEqual Right(JsonObject.Var("none" -> Empty))
		}
		"serialize Some(None) as {some:null}" in {
			writeAst(Some(None):Option[Option[String]]) mustEqual Right(JsonObject.Var("some" -> JsonNull))
		}
		"serialize Some(Some) as {some:value}" in {
			writeAst(Some(Some("hallo")):Option[Option[String]]) mustEqual Right(JsonObject.Var("some" -> JsonString("hallo")))
		}
	}

	"double nested option" should {
		"serialize None as {none:{}}" in {
			writeAst(None:Option[Option[Option[String]]]) mustEqual Right(JsonObject.Var("none" -> Empty))
		}
		"serialize Some(None) as {some:{none:{}}}" in {
			writeAst(Some(None):Option[Option[Option[String]]]) mustEqual Right(JsonObject.Var("some" -> JsonObject.Var("none" -> Empty)))
		}
		"serialize Some(Some(None)) as {some:{some:null}}" in {
			writeAst(Some(Some(None)):Option[Option[Option[String]]]) mustEqual Right(JsonObject.Var("some" -> JsonObject.Var("some" -> JsonNull)))
		}
		"serialize Some(Some(Some)) as {some:{some:value}}" in {
			writeAst(Some(Some(Some("hallo"))):Option[Option[Option[String]]]) mustEqual Right(JsonObject.Var("some" -> JsonObject.Var("some" -> JsonString("hallo"))))
		}
	}

	//------------------------------------------------------------------------------

	// TODO it seems in old times, we encoded ```none``` as ```{ "none": true }``` and now we use ```{ "none": {} }```
	//	JsonTrue
	def Empty:JsonValue	= JsonObject.Var()

	def readAst[T:JsonReader](json:JsonValue):Either[JsonError,T]	=
		JsonReader[T].convert(json).toEither

	def writeAst[T:JsonWriter](value:T):Either[JsonError,JsonValue]	=
		JsonWriter[T].convert(value).toEither
}
