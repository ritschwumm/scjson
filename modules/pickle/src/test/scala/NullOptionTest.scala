package scjson.pickle

import org.specs2.mutable._

import scjson.ast._
import scjson.pickle.protocol._

class NullOptionTest extends Specification {
	import StandardProtocol2._
	
	"plain option" should {
		"serialize None as null" in {
			doWrite(None:Option[String]) mustEqual JsonNull
		}
		"serialize Some as value" in {
			doWrite(Some("hallo"):Option[String]) mustEqual JsonString("hallo")
		}
	}
	
	"nested option" should {
		"serialize None as {none:true}" in {
			doWrite(None:Option[Option[String]]) mustEqual JsonObject.Var("none" -> JsonTrue)
		}
		"serialize Some(None) as {some:null}" in {
			doWrite(Some(None):Option[Option[String]]) mustEqual JsonObject.Var("some" -> JsonNull)
		}
		"serialize Some(Some) as {some:value}" in {
			doWrite(Some(Some("hallo")):Option[Option[String]]) mustEqual JsonObject.Var("some" -> JsonString("hallo"))
		}
	}
	
	"double nested option" should {
		"serialize None as {none:true}" in {
			doWrite(None:Option[Option[Option[String]]]) mustEqual JsonObject.Var("none" -> JsonTrue)
		}
		"serialize Some(None) as {some:{none:true}}" in {
			doWrite(Some(None):Option[Option[Option[String]]]) mustEqual JsonObject.Var("some" -> JsonObject.Var("none" -> JsonTrue))
		}
		"serialize Some(Some(None)) as {some:{some:null}}" in {
			doWrite(Some(Some(None)):Option[Option[Option[String]]]) mustEqual JsonObject.Var("some" -> JsonObject.Var("some" -> JsonNull))
		}
		"serialize Some(Some(Some)) as {some:{some:value}}" in {
			doWrite(Some(Some(Some("hallo"))):Option[Option[Option[String]]]) mustEqual JsonObject.Var("some" -> JsonObject.Var("some" -> JsonString("hallo")))
		}
	}
}
