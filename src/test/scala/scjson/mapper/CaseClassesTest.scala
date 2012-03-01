package scjson

import org.specs2.mutable._

import scjson._

import scjson.test._

// NOTE must be equalTo <==> mustEqual
class CaseClassesTest extends Specification {
	"simple case classes" should {
		val nativ	= SimpleCaseClass(false)
		val json	= JSONObject(Map(
				JSONString("")		-> JSONString("scjson.test.SimpleCaseClass"),
				JSONString("ok")	-> JSONBoolean(false)))
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
	
	"simple case objects" should {
		val nativ	= SimpleCaseObject
		val json	= JSONObject(Map(
				JSONString("")	-> JSONString("scjson.test.SimpleCaseObject$")))
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
	
	"inheriting param case classes" should {
		val nativ	= InheritingParamCaseClass("eins")
		val json	= JSONObject(Map(
				JSONString("")		-> JSONString("scjson.test.InheritingParamCaseClass"),
				JSONString("id")	-> JSONString("4711"),
				JSONString("name")	-> JSONString("eins")))
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
	
	"inheriting param case objects" should {
		val nativ	= InheritingParamCaseObject
		val json	= JSONObject(Map(
				JSONString("")		-> JSONString("scjson.test.InheritingParamCaseObject$"),
				JSONString("id")	-> JSONString("4711")))
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
	
	"inheriting field case classes" should {
		val nativ	= InheritingFieldCaseClass("eins")
		val json	= JSONObject(Map(
				JSONString("")		-> JSONString("scjson.test.InheritingFieldCaseClass"),
				JSONString("id")	-> JSONString("4711"),
				JSONString("name")	-> JSONString("eins")))
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
	
	"inheriting field case objects" should {
		val nativ	= InheritingFieldCaseObject
		val json	= JSONObject(Map(
				JSONString("")		-> JSONString("scjson.test.InheritingFieldCaseObject$"),
				JSONString("id")	-> JSONString("4711")))
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
	
	"case classes with weird fields names" should {
		val nativ	= WeirdNameCaseClass(1)
		val json	= JSONObject(Map(
				JSONString("")	-> JSONString("scjson.test.WeirdNameCaseClass"),
				JSONString("*")	-> JSONNumber(1)))
		"be serialized"		in { JSONMapper write nativ mustEqual json 	}
		"as deserialized"	in { JSONMapper read  json  mustEqual nativ	}
	}
}
