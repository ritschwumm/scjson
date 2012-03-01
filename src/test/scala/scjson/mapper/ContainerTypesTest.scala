package scjson

import org.specs2.mutable._

import scjson._
import scjson.JSONSerialization._

// NOTE must be equalTo <==> mustEqual
class ContainerTypesTest extends Specification {
	"Option some" should {
		val nativ	= Some("hallo")
		val json	= JSONString("hallo") 
		val standin	= "hallo"
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual standin	}
	}
	
	"Option none" should {
		val nativ	= None:Option[String]
		val json	= JSONNull         
		val standin	= null
		"be serialized"		in { JSONMapper write nativ	mustEqual json 		}
		"as deserialized"	in { JSONMapper read  json 	mustEqual standin	}
	}
	
	"Seq" should {
		val nativ	= Seq[Double](0.0, 1.0, 2.0)
		val json	= JSONArray(Seq(JSONNumber(0.0), JSONNumber(1.0), JSONNumber(2.0))) 
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
	
	"Map" should {
		val nativ	= Map[String,Int]("a"->1, "b"->2)
		val json	= JSONObject(Map(JSONString("a")->JSONNumber(1), JSONString("b")->JSONNumber(2)))
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
	
	"Pair" should {
		"roundtrip" in {
			val a	= Pair(1,1)
			val b	= JSONMapper write	a
			val c	= JSONMapper read	b
			a mustEqual c
		}
	}
}
