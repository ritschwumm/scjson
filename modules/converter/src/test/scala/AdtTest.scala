package scjson.converter

import minitest.*

import scutil.lang.*

import scjson.ast.*

import JsonFormat.{ given, * }

//------------------------------------------------------------------------------

object TestAdt {
	case object TestAdtObj						extends TestAdt
	final case class TestAdt1(a:Int)			extends TestAdt
	final case class TestAdt2(a:Int, b:String)	extends TestAdt

	object P {
		type Self	= TestAdt
		val Self	= TestAdt
		val TestAdtObj	= Prism.subType[Self,Self.TestAdtObj.type]
		val TestAdt1	= Prism.subType[Self,Self.TestAdt1]
		val TestAdt2	= Prism.subType[Self,Self.TestAdt2]
	}

	given TestAdtReader:JsonReader[TestAdt]	=
		sumReaderVar(
			"TestAdtObj"	-> subReader(coReader(TestAdtObj)),
			"TestAdt1"		-> subReader(cc1AutoReader[TestAdt1]),
			"TestAdt2"		-> subReader(cc2AutoReader[TestAdt2])
		)
	given TestAdtWriter:JsonWriter[TestAdt]	=
		sumWriterVar(
			"TestAdtObj"	-> subWriter(coWriter(TestAdtObj),		P.TestAdtObj.get),
			"TestAdt1"		-> subWriter(cc1AutoWriter[TestAdt1],	P.TestAdt1.get),
			"TestAdt2"		-> subWriter(cc2AutoWriter[TestAdt2],	P.TestAdt2.get)
		)
}

sealed trait TestAdt

//------------------------------------------------------------------------------

object AdtTest extends SimpleTestSuite {
	val Empty	= JsonValue.emptyObject

	test("auto reader arity cannot be too large") {
		assertDoesNotCompile("cc2AutoReader[TestAdt.TestAdt1]")
	}
	test("auto writer arity cannot be too large") {
		assertDoesNotCompile("cc2AutoWriter[TestAdt.TestAdt1]")
	}
	test("auto reader arity cannot be too small") {
		assertDoesNotCompile("cc1AutoReader[TestAdt.TestAdt2]")
	}
	test("auto writer arity cannot be too small") {
		assertDoesNotCompile("cc1AutoWriter[TestAdt.TestAdt2]")
	}

	test("adts should unparse object") {
		assertEquals(
			JsonWriter[TestAdt] convert TestAdt.TestAdtObj,
			Validated.valid(JsonValue.obj("TestAdtObj" -> Empty))
		)
	}
	test("adts should parse object") {
		assertEquals(
			JsonReader[TestAdt] convert JsonValue.obj("TestAdtObj" -> Empty),
			Validated.valid(TestAdt.TestAdtObj)
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
