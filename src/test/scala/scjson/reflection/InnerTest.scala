package scjson

import org.specs._
import org.specs.matcher._

import scutil.Implicits._

import scjson._
import scjson.reflection._

import scjson.test._

// TODO merge with OuterTest
class InnerTest extends Specification {
	val foo = Whip.Foo("hallo")
	val bar	= foo.Bar("bello")
		
	"Inners" should {
		"show no constructor parameters for toplevel object" in { 
			val constructor	= Reflector constructor Whip.getClass
			constructor mustEqual Some(List())
		}
		"show constructor parameters for level 1 classes" in { 
			val constructor	= Reflector constructor foo.getClass
			constructor mustEqual Some(List("str"))
		}
		"show constructor parameters for level 2 classes" in { 
			val constructor	= Reflector constructor bar.getClass
			constructor mustEqual Some(List("bong"))
		}
		
		"show no accessors for toplevel object" in { 
			val accessors	= Reflector accessors Whip.getClass
			accessors mustEqual List()
		}
		"show accessors for level 1 classes" in { 
			val accessors	= Reflector accessors foo.getClass
			accessors mustEqual List("str")
		}
		"show accessors for level 2 classes" in { 
			val accessors	= Reflector accessors bar.getClass
			accessors mustEqual List("bong")
		}
	}
}
