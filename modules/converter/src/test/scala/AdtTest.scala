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
			"TestAdt1"		-> subReader(cc1AutoReader[TestAdt1]),
			"TestAdt2"		-> subReader(cc2AutoReader[TestAdt2])
		)
	implicit val TestAdtWriter:JsonWriter[TestAdt]	=
		sumWriterVar(
			"TestAdt0"		-> subWriter(coWriter(TestAdt0),		P.TestAdt0.get),
			"TestAdt1"		-> subWriter(cc1AutoWriter[TestAdt1],	P.TestAdt1.get),
			"TestAdt2"		-> subWriter(cc2AutoWriter[TestAdt2],	P.TestAdt2.get)
		)
}

sealed trait TestAdt

//------------------------------------------------------------------------------

object AdtTest extends SimpleTestSuite {
	val Empty	= JsonValue.emptyObject

	test("adts should unparse 0") {
		assertEquals(
			JsonWriter[TestAdt] convert TestAdt.TestAdt0,
			Validated.valid(JsonValue.obj("TestAdt0" -> Empty))
		)
	}
	test("adts should parse 0") {
		assertEquals(
			JsonReader[TestAdt] convert JsonValue.obj("TestAdt0" -> Empty),
			Validated.valid(TestAdt.TestAdt0)
		)
	}

	test("adts should unparse 1") {
		assertEquals(
			JsonWriter[TestAdt] convert TestAdt.TestAdt1(7),
			Validated.valid(JsonValue.obj("TestAdt1" -> JsonValue.obj("a" -> JsonValue.fromInt(7))))
		)
	}
	test("adts should parse 1") {
		assertEquals(
			JsonReader[TestAdt] convert JsonValue.obj("TestAdt1" -> JsonValue.obj("a" -> JsonValue.fromInt(7))),
			Validated.valid(TestAdt.TestAdt1(7))
		)
	}

	test("adts should unparse 2") {
		assertEquals(
			JsonWriter[TestAdt] convert TestAdt.TestAdt2(7, "z"),
			Validated.valid(JsonValue.obj("TestAdt2" -> JsonValue.obj("a" -> JsonValue.fromInt(7), "b" -> JsonValue.fromString("z"))))
		)
	}
	test("adts should parse 2") {
		assertEquals(
			JsonReader[TestAdt] convert JsonValue.obj("TestAdt2" -> JsonValue.obj("a" -> JsonValue.fromInt(7), "b" -> JsonValue.fromString("z"))),
			Validated.valid(TestAdt.TestAdt2(7, "z"))
		)
	}
}
