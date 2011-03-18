package scjson.test

import org.specs._
import org.specs.matcher._

import scutil.Implicits._

import scjson._
import scjson.reflect._
import scjson.JSSerialization._

class PropertiesTest extends Specification {
	// TODO add case classes
	
	"CaseObject properties" should {
		"inherit outcoming params from parents" in { 
			val clazz	= Class forName "scjson.test.InheritingParamCaseObject$"
			val props	= Reflector reflect clazz 
			// println("### props: " + props)
			props mustEqual Some(Reflected(
					List(),
					List(Property fromPlain "id")))
		}
		
		"inherit outcoming fields from parents" in { 
			val clazz	= Class forName "scjson.test.InheritingFieldCaseObject$"
			val props	= Reflector reflect clazz 
			// println("### props: " + props)
			props mustEqual Some(Reflected(
					List(),
					List(Property fromPlain "id")))
		}
		
		"be allowed to have weird names" in {
			val clazz	= Class forName "scjson.test.WeirdNameCaseClass"
			val props	= Reflector reflect clazz 
			props mustEqual Some(Reflected(
					List(Property fromPlain "*"),
					List(Property fromPlain "*")))
		}
	}
}
