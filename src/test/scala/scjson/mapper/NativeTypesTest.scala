package scjson

import org.specs2.mutable._

import scjson._

// NOTE must be equalTo <==> mustEqual
class NativeTypesTest extends Specification {
	"Null" should {
		val nativ	= null
		val json	= JSONNull 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	"Boolean true" should {
		val nativ	= true
		val json	= JSONBoolean(true) 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	"Boolean false" should {
		val nativ	= false
		val json	= JSONBoolean(false) 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	// TODO why doesn't mustBe work with all these?
	"Int" should {
		val nativ	= 4711
		val json	= JSONNumber(4711) 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	"Long" should {
		val nativ	= 4711L
		val json	= JSONNumber(4711L) 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	"Float" should {
		val nativ	= 4711.0f
		val json	= JSONNumber(4711.0f) 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	"Double" should {
		val nativ	= 4711.0
		val json	= JSONNumber(4711.0) 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	"BigInt" should {
		val nativ	= BigInt(4711)
		val json	= JSONNumber(4711) 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	"BigDecimal" should {
		val nativ	= BigDecimal(4711)
		val json	= JSONNumber(4711) 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
	
	"String" should {
		val nativ	= "hallo"
		val json	= JSONString("hallo") 
		"be serialized"		in { JSONMapper write	nativ	mustEqual json 	}
		"as deserialized"	in { JSONMapper read	json	mustEqual nativ	}
	}
}
