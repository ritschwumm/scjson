package scjson.serialization

import org.specs2.mutable._

import scutil.Implicits._

import scjson._

class TypeClassTest extends Specification {
	sealed trait SimpleBase
	case object SimpleObject											extends SimpleBase
	case class	SimpleClass(ok:Boolean, x:Option[Int], y:Set[String])	extends SimpleBase
	
	object MyProtocol extends FullProtocol {
		implicit val SimpleObjectFormat:JSONFormat[SimpleObject.type]	= caseObjectJSONFormat(SimpleObject)
		implicit val SimpleClassFormat:JSONFormat[SimpleClass]			= caseClassJSONFormat3(SimpleClass.apply, SimpleClass.unapply)
		implicit val SimpleBaseFormat:JSONFormat[SimpleBase]			= caseClassSumJSONFormat(SimpleObjectFormat, SimpleClassFormat)
		implicit val SimpleSumFormat:JSONFormat[Any]					= sumJSONFormat[Any](IntJSONFormat,StringJSONFormat)
	}
	
	"foo" should {
		"bar" in { 
			import MyProtocol._
			
			val orig	= SimpleClass(true, Some(1), Set("hallo", "welt"))
			val json	= doWrite[SimpleBase](orig)
			val back	= doRead[SimpleBase](json)
			orig mustEqual back
		}
		
		"whibble" in { 
			import MyProtocol._
			
			val orig	= JSONString("hallo")
			val json	= doWrite[JSONString](orig)
			val back	= doRead[JSONString](json)
			orig mustEqual back
		}
		
		"foork" in {
			import MyProtocol._
			
			val orig1	= 1
			val json1	= SimpleSumFormat write orig1
			// println("json1=" + json1)
			val back1	= SimpleSumFormat read  json1
			orig1 mustEqual back1
			
			val orig2	= "2"
			val json2	= SimpleSumFormat write orig2
			// println("json2=" + json2)
			val back2	= SimpleSumFormat read  json2
			orig2 mustEqual back2
		}
	}
}
