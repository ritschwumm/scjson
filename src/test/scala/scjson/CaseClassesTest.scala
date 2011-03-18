package scjson.test

import org.specs._
import org.specs.matcher._

import scjson._
import scjson.JSSerialization._

// NOTE must be equalTo <==> mustEqual
class CaseClassesTest extends Specification {
	"simple case classes" should {
		val nativ	= SimpleCaseClass(false)
		val json	= JSObject(Map(
				JSString("_type")	-> JSString("scjson.test.SimpleCaseClass"),
				JSString("ok")		-> JSBoolean(false)))
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"simple case objects" should {
		val nativ	= SimpleCaseObject
		val json	= JSObject(Map(
				JSString("_type")	-> JSString("scjson.test.SimpleCaseObject$")))
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"inheriting param case classes" should {
		val nativ	= InheritingParamCaseClass("eins")
		val json	= JSObject(Map(
				JSString("_type")	-> JSString("scjson.test.InheritingParamCaseClass"),
				JSString("id")		-> JSString("4711"),
				JSString("name")	-> JSString("eins")))
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"inheriting param case objects" should {
		val nativ	= InheritingParamCaseObject
		val json	= JSObject(Map(
				JSString("_type")	-> JSString("scjson.test.InheritingParamCaseObject$"),
				JSString("id")		-> JSString("4711")))
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"inheriting field case classes" should {
		val nativ	= InheritingFieldCaseClass("eins")
		val json	= JSObject(Map(
				JSString("_type")	-> JSString("scjson.test.InheritingFieldCaseClass"),
				JSString("id")		-> JSString("4711"),
				JSString("name")	-> JSString("eins")))
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"inheriting field case objects" should {
		val nativ	= InheritingFieldCaseObject
		val json	= JSObject(Map(
				JSString("_type")	-> JSString("scjson.test.InheritingFieldCaseObject$"),
				JSString("id")		-> JSString("4711")))
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"case classes with weird fields names" should {
		val nativ	= WeirdNameCaseClass(1)
		val json	= JSObject(Map(
				JSString("_type")	-> JSString("scjson.test.WeirdNameCaseClass"),
				JSString("*")		-> JSNumber(1)))
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
}
