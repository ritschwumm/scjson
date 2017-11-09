package scjson.pickle

import org.specs2.mutable._

import scjson.ast._
import scjson.pickle.protocol._

class NullOptionTest extends Specification {
	import StandardProtocol._
	
	"plain option" should {
		"serialize None as null" in {
			doWrite(None:Option[String]) mustEqual JSONNull
		}
		"serialize Some as value" in {
			doWrite(Some("hallo"):Option[String]) mustEqual JSONString("hallo")
		}
	}
	
	"nested option" should {
		"serialize None as {none:true}" in {
			doWrite(None:Option[Option[String]]) mustEqual JSONObject.Var("none" -> JSONTrue)
		}
		"serialize Some(None) as {some:null}" in {
			doWrite(Some(None):Option[Option[String]]) mustEqual JSONObject.Var("some" -> JSONNull)
		}
		"serialize Some(Some) as {some:value}" in {
			doWrite(Some(Some("hallo")):Option[Option[String]]) mustEqual JSONObject.Var("some" -> JSONString("hallo"))
		}
	}
	
	"double nested option" should {
		"serialize None as {none:true}" in {
			doWrite(None:Option[Option[Option[String]]]) mustEqual JSONObject.Var("none" -> JSONTrue)
		}
		"serialize Some(None) as {some:{none:true}}" in {
			doWrite(Some(None):Option[Option[Option[String]]]) mustEqual JSONObject.Var("some" -> JSONObject.Var("none" -> JSONTrue))
		}
		"serialize Some(Some(None)) as {some:{some:null}}" in {
			doWrite(Some(Some(None)):Option[Option[Option[String]]]) mustEqual JSONObject.Var("some" -> JSONObject.Var("some" -> JSONNull))
		}
		"serialize Some(Some(Some)) as {some:{some:value}}" in {
			doWrite(Some(Some(Some("hallo"))):Option[Option[Option[String]]]) mustEqual JSONObject.Var("some" -> JSONObject.Var("some" -> JSONString("hallo")))
		}
	}
}
