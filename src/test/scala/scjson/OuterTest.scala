package scjson.test

import org.specs._
import org.specs.matcher._

import scutil.Implicits._

import scjson._
import scjson.reflect._
import scjson.JSSerialization._

object Outer {
	import Outers._
	
	val all:Seq[Outer] = Seq(
		aborigines
	)
}

sealed abstract class Outer(val id:String)

object Outers {
	case object aborigines		extends Outer("aborigines")
}
		
class OuterTest extends Specification {
	"Outers" should {
		"just work" in { 
			val clazz	= Class forName "scjson.test.Outers$aborigines$"
			val props	= Reflector reflect clazz 
			// println("### props: " + props)
			props mustEqual Some(Reflected(List(),List(Property("id", "id"))))
		}
	}
}
