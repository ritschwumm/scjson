package scjson.pickle

import org.specs2.mutable._

import scjson.ast._
import scjson.pickle.protocol._

sealed trait SimpleBase
case object SimpleObject													extends SimpleBase
final case class	SimpleClass(ok:Boolean, x:Option[Int], y:Set[String])	extends SimpleBase
	
class RoundtripTest extends Specification {
	object MyProtocol extends StandardProtocol {
		implicit lazy val SimpleSumFormat:Format[Any]					= objectSumFormat[Any](IntFormat, StringFormat)
		
		implicit lazy val SimpleBaseFormat:Format[SimpleBase]			= objectSumFormat(SimpleObjectFormat, SimpleClassFormat)
		implicit lazy val SimpleObjectFormat:Format[SimpleObject.type]	= caseObjectFormat(SimpleObject)
		implicit lazy val SimpleClassFormat:Format[SimpleClass]			= caseClassFormat3(SimpleClass.apply, SimpleClass.unapply)
	}
	
	import MyProtocol._
	
	//------------------------------------------------------------------------------
	
	"roundtrips" should {
		"work for a simple String" in {
			val orig	= JsonString("hallo")
			val json	= doWrite[JsonString](orig)
			val back	= doReadUnsafe[JsonString](json)
			orig mustEqual back
		}
		
		"work with primitive types in simple sums" in {
			val orig1	= 1
			val json1	= SimpleSumFormat get orig1
			// println("json1=" + json1)
			val back1	= SimpleSumFormat set  json1
			orig1 mustEqual back1
		}
		
		"work with AnyRef in simple sums" in {
			val orig2	= "2"
			val json2	= SimpleSumFormat get orig2
			// println("json2=" + json2)
			val back2	= SimpleSumFormat set  json2
			orig2 mustEqual back2
		}
		
		"work for ADTs" in {
			val orig	= SimpleClass(true, Some(1), Set("hallo", "welt"))
			val json	= doWrite[SimpleBase](orig)
			val back	= doReadUnsafe[SimpleBase](json)
			orig mustEqual back
		}
	}
}
