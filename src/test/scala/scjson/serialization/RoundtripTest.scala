package scjson.serialization

import org.specs2.mutable._

import scjson._

class RoundtripTest extends Specification {
	sealed trait SimpleBase
	case object SimpleObject											extends SimpleBase
	case class	SimpleClass(ok:Boolean, x:Option[Int], y:Set[String])	extends SimpleBase
	
	object MyProtocol extends FullProtocol {
		implicit lazy val SimpleSumFormat:Format[Any]					= objectSumFormat[Any](IntFormat, StringFormat)
		
		implicit lazy val SimpleBaseFormat:Format[SimpleBase]			= objectSumFormat(SimpleObjectFormat, SimpleClassFormat)
		implicit lazy val SimpleObjectFormat:Format[SimpleObject.type]	= caseObjectFormat(SimpleObject)
		implicit lazy val SimpleClassFormat:Format[SimpleClass]			= caseClassFormat3(SimpleClass.apply, SimpleClass.unapply)
	}
	
	import MyProtocol._
	
	//------------------------------------------------------------------------------
	
	"roundtrips" should {
		"work for a simple String" in {
			val orig	= JSONString("hallo")
			val json	= doWrite[JSONString](orig)
			val back	= doRead[JSONString](json)
			orig mustEqual back
		}
		
		"work with primitive types in simple sums" in {
			val orig1	= 1
			val json1	= SimpleSumFormat write orig1
			// println("json1=" + json1)
			val back1	= SimpleSumFormat read  json1
			orig1 mustEqual back1
		}
		
		"work with AnyRef in simple sums" in {
			val orig2	= "2"
			val json2	= SimpleSumFormat write orig2
			// println("json2=" + json2)
			val back2	= SimpleSumFormat read  json2
			orig2 mustEqual back2
		}
		
		"work for ADTs" in {
			val orig	= SimpleClass(true, Some(1), Set("hallo", "welt"))
			val json	= doWrite[SimpleBase](orig)
			val back	= doRead[SimpleBase](json)
			orig mustEqual back
		}
	}
}
