package scjson.converter

import minitest.*

import scutil.lang.*

import scjson.ast.*

import JsonFormat.{ given, * }

//------------------------------------------------------------------------------

object TestEnumAdt {
	val P	= Prism.Gen[TestEnumAdt]

	given TestEnumAdtReader:JsonReader[TestEnumAdt]	=
		sumReaderVar(
			"Obj"		-> subReader(coReader(TestEnumAdt.Obj)),
			"Class0"	-> subReader(cc0AutoReader[TestEnumAdt.Class0]),
			"Class1"	-> subReader(cc1AutoReader[TestEnumAdt.Class1]),
			"Class2"	-> subReader(cc2AutoReader[TestEnumAdt.Class2])
		)
	given TestEnumAdtWriter:JsonWriter[TestEnumAdt]	=
		sumWriterVar(
			"Obj"		-> subWriter(coWriter(TestEnumAdt.Obj),			P.Obj.get),
			"Class0"	-> subWriter(cc0AutoWriter[TestEnumAdt.Class0],	P.Class0.get),
			"Class1"	-> subWriter(cc1AutoWriter[TestEnumAdt.Class1],	P.Class1.get),
			"Class2"	-> subWriter(cc2AutoWriter[TestEnumAdt.Class2],	P.Class2.get)
		)
}

enum TestEnumAdt {
	case Obj
	case Class0()
	case Class1(a:Int)
	case Class2(a:Int, b:String)
}

//------------------------------------------------------------------------------

object EnumAdtTest extends SimpleTestSuite {
	val Empty	= JsonValue.emptyObject

	test("auto reader arity cannot be too large") {
		assertDoesNotCompile("cc2AutoReader[TestEnumAdt.Class1]")
	}
	test("auto writer arity cannot be too large") {
		assertDoesNotCompile("cc2AutoWriter[TestEnumAdt.Class1]")
	}
	test("auto reader arity cannot be too small") {
		assertDoesNotCompile("cc1AutoReader[TestEnumAdt.Class2]")
	}
	test("auto writer arity cannot be too small") {
		assertDoesNotCompile("cc1AutoWriter[TestEnumAdt.Class2]")
	}

	test("adts should unparse object") {
		assertEquals(
			JsonWriter[TestEnumAdt].convert(TestEnumAdt.Obj),
			Validated.valid(JsonValue.obj("Obj" -> Empty))
		)
	}
	test("adts should parse object") {
		assertEquals(
			JsonReader[TestEnumAdt].convert(JsonValue.obj("Obj" -> Empty)),
			Validated.valid(TestEnumAdt.Obj)
		)
	}

	test("adts should unparse 0") {
		assertEquals(
			JsonWriter[TestEnumAdt].convert(TestEnumAdt.Class0()),
			Validated.valid(JsonValue.obj("Class0" -> Empty))
		)
	}
	test("adts should parse 0") {
		assertEquals(
			JsonReader[TestEnumAdt].convert(JsonValue.obj("Class0" -> Empty)),
			Validated.valid(TestEnumAdt.Class0())
		)
	}

	test("adts should unparse 1") {
		assertEquals(
			JsonWriter[TestEnumAdt].convert(TestEnumAdt.Class1(7)),
			Validated.valid(JsonValue.obj("Class1" -> JsonValue.obj("a" -> JsonValue.fromInt(7))))
		)
	}
	test("adts should parse 1") {
		assertEquals(
			JsonReader[TestEnumAdt].convert(JsonValue.obj("Class1" -> JsonValue.obj("a" -> JsonValue.fromInt(7)))),
			Validated.valid(TestEnumAdt.Class1(7))
		)
	}

	test("adts should unparse 2") {
		assertEquals(
			JsonWriter[TestEnumAdt].convert(TestEnumAdt.Class2(7, "z")),
			Validated.valid(JsonValue.obj("Class2" -> JsonValue.obj("a" -> JsonValue.fromInt(7), "b" -> JsonValue.fromString("z"))))
		)
	}
	test("adts should parse 2") {
		assertEquals(
			JsonReader[TestEnumAdt].convert(JsonValue.obj("Class2" -> JsonValue.obj("a" -> JsonValue.fromInt(7), "b" -> JsonValue.fromString("z")))),
			Validated.valid(TestEnumAdt.Class2(7, "z"))
		)
	}
}
