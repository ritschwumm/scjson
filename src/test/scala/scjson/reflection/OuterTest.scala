package scjson

import org.specs._
import org.specs.matcher._

import scutil.Implicits._

import scjson._
import scjson.reflection._
import scjson.JSSerialization._

// TODO merge with InnerTest
class OuterTest extends Specification {
	val clazz	= Class forName "scjson.test.Outers$aborigines$"
	"Outers" should {
		"not have constructor parameters" in { 
			val constructor	= Reflector constructor clazz
			constructor mustEqual Some(List())
		}
		"inherit accessors" in { 
			val accessors	= Reflector accessors clazz
			accessors mustEqual List("id")
		}
	}
}
