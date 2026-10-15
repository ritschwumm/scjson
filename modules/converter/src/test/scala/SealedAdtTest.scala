package scjson.converter

import minitest.*

import scutil.lang.*

import scjson.ast.*

import JsonFormat.{ given, * }

//------------------------------------------------------------------------------

object SealedTestAdt {
	case object Obj								extends SealedTestAdt
	final case class Class0()					extends SealedTestAdt
	final case class Class1(a:Int)				extends SealedTestAdt
	final case class Class2(a:Int, b:String)	extends SealedTestAdt

	object P {
		val Obj		= Prism.subType[SealedTestAdt, SealedTestAdt.Obj.type]
		val Class0	= Prism.subType[SealedTestAdt, SealedTestAdt.Class0]
		val Class1	= Prism.subType[SealedTestAdt, SealedTestAdt.Class1]
		val Class2	= Prism.subType[SealedTestAdt, SealedTestAdt.Class2]
	}

	given TestAdtReader:JsonReader[SealedTestAdt]	=
		sumReaderVar(
			"Obj"		-> subReader(coReader(Obj)),
			"Class0"	-> subReader(cc0AutoReader[Class0]),
			"Class1"	-> subReader(cc1AutoReader[Class1]),
			"Class2"	-> subReader(cc2AutoReader[Class2])
		)
	given TestAdtWriter:JsonWriter[SealedTestAdt]	=
		sumWriterVar(
			"Obj"		-> subWriter(coWriter(Obj),			P.Obj.get),
			"Class0"	-> subWriter(cc0AutoWriter[Class0],	P.Class0.get),
			"Class1"	-> subWriter(cc1AutoWriter[Class1],	P.Class1.get),
			"Class2"	-> subWriter(cc2AutoWriter[Class2],	P.Class2.get)
		)
}

sealed trait SealedTestAdt

//------------------------------------------------------------------------------

object SealedAdtTest extends SimpleTestSuite {
	val Empty	= JsonValue.emptyObject

	test("auto reader arity cannot be too large") {
		assertDoesNotCompile("cc2AutoReader[SealedTestAdt.Class1]")
	}
	test("auto writer arity cannot be too large") {
		assertDoesNotCompile("cc2AutoWriter[SealedTestAdt.Class1]")
	}
	test("auto reader arity cannot be too small") {
		assertDoesNotCompile("cc1AutoReader[SealedTestAdt.Class2]")
	}
	test("auto writer arity cannot be too small") {
		assertDoesNotCompile("cc1AutoWriter[SealedTestAdt.Class2]")
	}

	test("adts should unparse object") {
		assertEquals(
			JsonWriter[SealedTestAdt].convert(SealedTestAdt.Obj),
			Validated.valid(JsonValue.obj("Obj" -> Empty))
		)
	}
	test("adts should parse object") {
		assertEquals(
			JsonReader[SealedTestAdt].convert(JsonValue.obj("Obj" -> Empty)),
			Validated.valid(SealedTestAdt.Obj)
		)
	}

	test("adts should unparse 0") {
		assertEquals(
			JsonWriter[SealedTestAdt].convert(SealedTestAdt.Class0()),
			Validated.valid(JsonValue.obj("Class0" -> Empty))
		)
	}
	test("adts should parse 0") {
		assertEquals(
			JsonReader[SealedTestAdt].convert(JsonValue.obj("Class0" -> Empty)),
			Validated.valid(SealedTestAdt.Class0())
		)
	}

	test("adts should unparse 1") {
		assertEquals(
			JsonWriter[SealedTestAdt].convert(SealedTestAdt.Class1(7)),
			Validated.valid(JsonValue.obj("Class1" -> JsonValue.obj("a" -> JsonValue.fromInt(7))))
		)
	}
	test("adts should parse 1") {
		assertEquals(
			JsonReader[SealedTestAdt].convert(JsonValue.obj("Class1" -> JsonValue.obj("a" -> JsonValue.fromInt(7)))),
			Validated.valid(SealedTestAdt.Class1(7))
		)
	}

	test("adts should unparse 2") {
		assertEquals(
			JsonWriter[SealedTestAdt].convert(SealedTestAdt.Class2(7, "z")),
			Validated.valid(JsonValue.obj("Class2" -> JsonValue.obj("a" -> JsonValue.fromInt(7), "b" -> JsonValue.fromString("z"))))
		)
	}
	test("adts should parse 2") {
		assertEquals(
			JsonReader[SealedTestAdt].convert(JsonValue.obj("Class2" -> JsonValue.obj("a" -> JsonValue.fromInt(7), "b" -> JsonValue.fromString("z")))),
			Validated.valid(SealedTestAdt.Class2(7, "z"))
		)
	}
}
