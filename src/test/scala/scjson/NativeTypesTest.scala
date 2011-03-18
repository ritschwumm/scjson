package scjson.test

import org.specs._
import org.specs.matcher._

import scjson._
import scjson.JSSerialization._

// NOTE must be equalTo <==> mustEqual
class NativeTypesTest extends Specification {
	"Null" should {
		val nativ	= null
		val json	= JSNull 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustBe    nativ	}
	}
	
	"Boolean true" should {
		val nativ	= true
		val json	= JSBoolean(true) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"Boolean false" should {
		val nativ	= false
		val json	= JSBoolean(false) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"Int" should {
		val nativ	= 4711
		val json	= JSNumber(4711) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"Long" should {
		val nativ	= 4711L
		val json	= JSNumber(4711L) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"Float" should {
		val nativ	= 4711.0f
		val json	= JSNumber(4711.0f) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"Double" should {
		val nativ	= 4711.0
		val json	= JSNumber(4711.0) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"BigInt" should {
		val nativ	= BigInt(4711)
		val json	= JSNumber(4711) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"BigDecimal" should {
		val nativ	= BigDecimal(4711)
		val json	= JSNumber(4711) 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
	
	"String" should {
		val nativ	= "hallo"
		val json	= JSString("hallo") 
		"be serialized"		in { serialize(nativ)	mustEqual json 	}
		"as deserialized"	in { deserialize(json)	mustEqual nativ	}
	}
}
