package scjson.test

import org.specs._
import org.specs.matcher._

import scjson._
import scjson.JSSerialization._

// NOTE must be equalTo <==> mustEqual
class ContainerTypesTest extends Specification {
	"Option some" should {
		val nativ	= Some("hallo")
		val json	= JSString("hallo") 
		val standin	= "hallo"
		"be serialized"		in { serialize(nativ)	mustEqual json 		}
		"as deserialized"	in { deserialize(json)	mustEqual standin	}
	}
	
	"Option none" should {
		val nativ	= None:Option[String]
		val json	= JSNull         
		val standin	= null
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustBe    null	}
	}
	
	"Seq" should {
		val nativ	= Seq[Double](0.0, 1.0, 2.0)
		val json	= JSArray(Seq(JSNumber(0.0), JSNumber(1.0), JSNumber(2.0))) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"Map" should {
		val nativ	= Map[String,Int]("a"->1, "b"->2)
		val json	= JSObject(Map(JSString("a")->JSNumber(1), JSString("b")->JSNumber(2)))
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
}
