package scjson.pickle

import minitest._

import scjson.ast._
import scjson.pickle.protocol._

sealed trait TestEnum
case object TestEnum0	extends TestEnum
case object TestEnum1	extends TestEnum
case object TestEnum2	extends TestEnum

sealed trait TestSum
case object TestSum0					extends TestSum
final case class TestSum1(a:Int)		extends TestSum
final case class TestSum2(a:Int, b:Int)	extends TestSum

sealed trait TestADT
case object TestADT0					extends TestADT
final case class TestADT1(a:Int)		extends TestADT
final case class TestADT2(a:Int, b:Int)	extends TestADT

object FormatTest extends SimpleTestSuite {
	object MyProtocol extends StandardProtocol {
		implicit val TestEnum_F:Format[TestEnum]	=
			enumFormat(Seq[(String,TestEnum)](
				"0"	-> TestEnum0,
				"1"	-> TestEnum1,
				"2"	-> TestEnum2)
			)

		implicit val TestADT_F:Format[TestADT]	=
			caseClassSumFormat(
				"0"	-> TestADT0_F,
				"1"	-> TestADT1_F,
				"2"	-> TestADT2_F
			)
		implicit lazy val TestADT0_F	= caseObjectFormat(TestADT0)
		implicit lazy val TestADT1_F	= caseClassFormat1(TestADT1.apply, TestADT1.unapply)
		implicit lazy val TestADT2_F	= caseClassFormat2(TestADT2.apply, TestADT2.unapply)
	}

	import MyProtocol._

	//------------------------------------------------------------------------------

	test("serialization should encode an enum as expected") {
		assertEquals(
			doWrite[TestEnum](TestEnum1)	,
			JsonString("1")
		)
	}
	test("serialization should decode an enum as expected") {
		assertEquals(
			doReadUnsafe[TestEnum](JsonString("1")),
			TestEnum1
		)
	}

	//------------------------------------------------------------------------------

	test("serialization should encode an adt constructor with 0 parameters as expected") {
		val data	= TestADT0
		val json	= JsonObject.Var(
			"" -> JsonString("0")
		)
		assertEquals(
			doWrite[TestADT](data),
			json
		)
	}
	test("serialization should encode an adt constructor with 1 parameter as expected") {
		val data	= TestADT1(4711)
		val json	= JsonObject.Var(
			""	-> JsonString("1"),
			"a"	-> JsonNumber(4711)
		)
		assertEquals(
			doWrite[TestADT](data),
			json
		)

	}
	test("serialization should encode an adt constructor with 2 parameters as expected") {
		val data	= TestADT2(1337,4711)
		val json	= JsonObject.Var(
			""	-> JsonString("2"),
			"a"	-> JsonNumber(1337),
			"b"	-> JsonNumber(4711)
		)
		assertEquals(
			doWrite[TestADT](data),
			json
		)
	}

	test("serialization should decode an adt constructor with 0 parameters as expected") {
		val data	= TestADT0
		val json	= JsonObject.Var(
			"" -> JsonString("0")
		)
		assertEquals(
			doReadUnsafe[TestADT](json),
			data
		)
	}
	test("serialization should decode an adt constructor with 1 parameter as expected") {
		val data	= TestADT1(4711)
		val json	= JsonObject.Var(
			""	-> JsonString("1"),
			"a"	-> JsonNumber(4711)
		)
		assertEquals(
			doReadUnsafe[TestADT](json),
			data
		)
	}
	test("serialization should decode an adt constructor with 2 parameters as expected") {
		val data	= TestADT2(1337,4711)
		val json	= JsonObject.Var(
			""	-> JsonString("2"),
			"a"	-> JsonNumber(1337),
			"b"	-> JsonNumber(4711)
		)
		assertEquals(
			doReadUnsafe[TestADT](json),
			data
		)
	}
}
