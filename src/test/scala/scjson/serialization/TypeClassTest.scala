package scjson.serialization

import org.specs._
import org.specs.matcher._

import scutil.Implicits._

import scjson._

class TypeClassTest extends Specification {
	sealed trait SimpleBase
	case object SimpleObject											extends SimpleBase
	case class	SimpleClass(ok:Boolean, x:Option[Int], y:Set[String])	extends SimpleBase
	
	object MyProtocol extends FullProtocol {
		implicit val SimpleObjectFormat:Format[SimpleObject.type]	= caseObjectFormat(SimpleObject)
		implicit val SimpleClassFormat:Format[SimpleClass]			= caseClassFormat3(SimpleClass.apply, SimpleClass.unapply)
		implicit val SimpleBaseFormat:Format[SimpleBase]			= caseClassSumFormat(SimpleObjectFormat, SimpleClassFormat)
	}
	
	"foo" should {
		"bar" in { 
			import MyProtocol._
			import Operations._
			
			val orig	= SimpleClass(true, Some(1), Set("hallo", "welt"))
			val json	= doWrite[SimpleBase](orig)
			val back	= doRead[SimpleBase](json)
			orig mustEqual back
		}
		
		"whibble" in { 
			import MyProtocol._
			import Operations._
			
			val orig	= JSString("hallo")
			val json	= doWrite[JSString](orig)
			val back	= doRead[JSString](json)
			orig mustEqual back
		}
	}
}
