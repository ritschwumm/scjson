package scjson.serialization

import org.specs2.mutable._

import scutil.Implicits._

import scjson._

class FormatTest extends Specification {
	sealed trait TestEnum
	case object TestEnum0	extends TestEnum
	case object TestEnum1	extends TestEnum
	case object TestEnum2	extends TestEnum
	
	sealed trait TestSum
	case object TestSum0				extends TestSum
	case class TestSum1(a:Int)			extends TestSum
	case class TestSum2(a:Int, b:Int)	extends TestSum
	
	sealed trait TestADT
	case object TestADT0				extends TestADT
	case class TestADT1(a:Int)			extends TestADT
	case class TestADT2(a:Int, b:Int)	extends TestADT
	
	object MyProtocol extends FullProtocol {
		implicit val TestEnum_F:Format[TestEnum]	= enumFormat(Seq(
				"0"	-> TestEnum0,
				"1"	-> TestEnum1,
				"2"	-> TestEnum2))
				
		implicit val TestSum_F:Format[TestSum]	= objectSumFormat[TestSum](
				"0"	-> TestSum0_F,
				"1"	-> TestSum1_F,
				"2"	-> TestSum2_F)
		implicit lazy val TestSum0_F	= caseObjectFormat(TestSum0)
		implicit lazy val TestSum1_F	= caseClassFormat1(TestSum1.apply, TestSum1.unapply)
		implicit lazy val TestSum2_F	= caseClassFormat2(TestSum2.apply, TestSum2.unapply)
		
		implicit val TestADT_F:Format[TestADT]	= caseClassSumFormat(
				"0"	-> TestADT0_F,
				"1"	-> TestADT1_F,
				"2"	-> TestADT2_F)
		implicit lazy val TestADT0_F	= caseObjectFormat(TestADT0)
		implicit lazy val TestADT1_F	= caseClassFormat1(TestADT1.apply, TestADT1.unapply)
		implicit lazy val TestADT2_F	= caseClassFormat2(TestADT2.apply, TestADT2.unapply)
	}
	
	import MyProtocol._
	
	//------------------------------------------------------------------------------
	
	"serialization should" should {
		"encode an enum as expected" in {
			doWrite[TestEnum](TestEnum1)		mustEqual	JSONString("1")
		}
		"decode an enum as expected" in {
			doRead[TestEnum](JSONString("1"))	mustEqual	TestEnum1
		}
		
		//------------------------------------------------------------------------------
		
		"encode a sum constructor with 0 parameters as expected" in {
			val data	= TestSum0
			val json	= JSONVarObject(
				"0"	-> JSONVarObject()
			)
			doWrite[TestSum](data)	mustEqual	json
		}
		"encode a sum constructor with 1 parameter as expected" in {
			val data	= TestSum1(4711)
			val json	= JSONVarObject(
				"1"	-> JSONVarObject(
					"a"	-> JSONNumber(4711)
				)
			)
			doWrite[TestSum](data)	mustEqual	json
		}
		"encode a sum constructor with 2 parameters as expected" in {
			val data	= TestSum2(1337,4711)
			val json	= JSONVarObject(
				"2"	-> JSONVarObject(
					"a"	-> JSONNumber(1337),
					"b"	-> JSONNumber(4711)
				)
			)
			doWrite[TestSum](data)	mustEqual	json
		}
		
		"decode a sum constructor with 0 parameters as expected" in {
			val data	= TestSum0
			val json	= JSONVarObject(
				"0"	-> JSONVarObject()
			)
			doRead[TestSum](json)	mustEqual	data
		}
		"decode a sum constructor with 1 parameter as expected" in {
			val data	= TestSum1(4711)
			val json	= JSONVarObject(
				"1"	-> JSONVarObject(
					"a"	-> JSONNumber(4711)
				)
			)
			doRead[TestSum](json)	mustEqual	data
		}
		"decode a sum constructor with 2 parameters as expected" in {
			val data	= TestSum2(1337,4711)
			val json	= JSONVarObject(
				"2"	-> JSONVarObject(
					"a"	-> JSONNumber(1337),
					"b"	-> JSONNumber(4711)
				)
			)
			doRead[TestSum](json)	mustEqual	data
		}
		//------------------------------------------------------------------------------
		
		"encode an adt constructor with 0 parameters as expected" in {
			val data	= TestADT0
			val json	= JSONVarObject(
				"" -> JSONString("0")
			)
			doWrite[TestADT](data)	mustEqual	json
		}
		"encode an adt constructor with 1 parameter as expected" in {
			val data	= TestADT1(4711)
			val json	= JSONVarObject(
				""	-> JSONString("1"),
				"a"	-> JSONNumber(4711)
			)
			doWrite[TestADT](data)	mustEqual	json
			
		}
		"encode an adt constructor with 2 parameters as expected" in {
			val data	= TestADT2(1337,4711)
			val json	= JSONVarObject(
				""	-> JSONString("2"),
				"a"	-> JSONNumber(1337),
				"b"	-> JSONNumber(4711)
			)
			doWrite[TestADT](data)	mustEqual	json
		}
		
		"decode an adt constructor with 0 parameters as expected" in {
			val data	= TestADT0
			val json	= JSONVarObject(
				"" -> JSONString("0")
			)
			doRead[TestADT](json)	mustEqual	data
		}
		"decode an adt constructor with 1 parameter as expected" in {
			val data	= TestADT1(4711)
			val json	= JSONVarObject(
				""	-> JSONString("1"),
				"a"	-> JSONNumber(4711)
			)
			doRead[TestADT](json)	mustEqual	data
		}
		"decode an adt constructor with 2 parameters as expected" in {
			val data	= TestADT2(1337,4711)
			val json	= JSONVarObject(
				""	-> JSONString("2"),
				"a"	-> JSONNumber(1337),
				"b"	-> JSONNumber(4711)
			)
			doRead[TestADT](json)	mustEqual	data
		}
	}
}
