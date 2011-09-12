package scjson

import org.specs._
import org.specs.matcher._

import scutil.Implicits._

import scjson._
import scjson.reflection._
import scjson.JSSerialization._

class PropertiesTest extends Specification {
	// TODO add case classes
	
	"CaseObject properties" should {
		"inherit accessors from parents" in { 
			val clazz		= Class forName "scjson.test.InheritingParamCaseObject$"
			val constructor	= Reflector constructor	clazz
			val accessors	= Reflector accessors	clazz
			constructor mustEqual Some(List())
			accessors mustEqual List("id")
		}
		
		"inherit accessors from parents" in { 
			val clazz		= Class forName "scjson.test.InheritingFieldCaseObject$"
			val constructor	= Reflector constructor	clazz
			val accessors	= Reflector accessors	clazz
			constructor mustEqual Some(List())
			accessors mustEqual List("id")
		}
		
		"be allowed to have weird names" in {
			val clazz		= Class forName "scjson.test.WeirdNameCaseClass"
			val constructor	= Reflector constructor	clazz
			val accessors	= Reflector accessors	clazz
			// println("### constructor=" + constructor)
			// println("### accessors=" + accessors)
			constructor	mustEqual Some(List("*"))
			accessors	mustEqual List("*")
		}
	}
}
