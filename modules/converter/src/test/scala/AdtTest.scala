package scjson.converter

import minitest._

import scutil.lang._

import scjson.ast._

import JsonFormat._

//------------------------------------------------------------------------------

object TestAdt {
	case object TestAdt0						extends TestAdt
	final case class TestAdt1(a:Int)			extends TestAdt
	final case class TestAdt2(a:Int, b:String)	extends TestAdt

	object P {
		type Self	= TestAdt
		val Self	= TestAdt
		val TestAdt0	= Prism.Gen[Self,Self.TestAdt0.type]
		val TestAdt1	= Prism.Gen[Self,Self.TestAdt1]
		val TestAdt2	= Prism.Gen[Self,Self.TestAdt2]
	}

	implicit val TestAdtReader:JsonReader[TestAdt]	=
		sumReaderVar(
			"TestAdt0"		-> subReader(coReader(TestAdt0)),
			"TestAdt1"		-> subReader(cc1AutoReader(TestAdt1.apply)),
			"TestAdt2"		-> subReader(cc2AutoReader(TestAdt2.apply))
		)
	implicit val TestAdtWriter:JsonWriter[TestAdt]	=
		sumWriterVar(
			"TestAdt0"		-> subWriter(coWriter(TestAdt0),				P.TestAdt0.get),
			"TestAdt1"		-> subWriter(cc1AutoWriter(TestAdt1.unapply),	P.TestAdt1.get),
			"TestAdt2"		-> subWriter(cc2AutoWriter(TestAdt2.unapply),	P.TestAdt2.get)
		)
}

sealed trait TestAdt

//------------------------------------------------------------------------------

object AdtTest extends SimpleTestSuite {
	val Empty	= JsonObject.Var()

	test("adts should unparse 0") {
		assertEquals(
			JsonWriter[TestAdt] convert TestAdt.TestAdt0,
			Validated.good(JsonObject.Var("TestAdt0" -> Empty))
		)
	}
	test("adts should parse 0") {
		assertEquals(
			JsonReader[TestAdt] convert JsonObject.Var("TestAdt0" -> Empty),
			Validated.good(TestAdt.TestAdt0)
		)
	}

	test("adts should unparse 1") {
		assertEquals(
			JsonWriter[TestAdt] convert TestAdt.TestAdt1(7),
			Validated.good(JsonObject.Var("TestAdt1" -> JsonObject.Var("a" -> JsonNumber(7))))
		)
	}
	test("adts should parse 1") {
		assertEquals(
			JsonReader[TestAdt] convert JsonObject.Var("TestAdt1" -> JsonObject.Var("a" -> JsonNumber(7))),
			Validated.good(TestAdt.TestAdt1(7))
		)
	}

	test("adts should unparse 2") {
		assertEquals(
			JsonWriter[TestAdt] convert TestAdt.TestAdt2(7, "z"),
			Validated.good(JsonObject.Var("TestAdt2" -> JsonObject.Var("a" -> JsonNumber(7), "b" -> JsonString("z"))))
		)
	}
	test("adts should parse 2") {
		assertEquals(
			JsonReader[TestAdt] convert JsonObject.Var("TestAdt2" -> JsonObject.Var("a" -> JsonNumber(7), "b" -> JsonString("z"))),
			Validated.good(TestAdt.TestAdt2(7, "z"))
		)
	}
}
