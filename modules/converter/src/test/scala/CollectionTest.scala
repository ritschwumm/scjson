package scjson.converter

import minitest._

import scutil.lang._

import scjson.ast._

import JsonFormat._

object CollectionTest extends SimpleTestSuite {
	test("special collections should serialize string maps") {
		assertEquals(
			JsonWriter[Map[String,Int]] convert Map("a" -> 1),
			Validated.valid(JsonValue.obj("a" -> JsonValue.fromInt(1)))
		)
	}
	test("special collections should serialize int maps") {
		assertEquals(
			JsonWriter[Map[Int,Int]] convert Map(2 -> 3),
			Validated.valid(JsonValue.obj("2" -> JsonValue.fromInt(3)))
		)
	}
	test("special collections should serialize long maps") {
		assertEquals(
			JsonWriter[Map[Long,Int]] convert Map(4L -> 5),
			Validated.valid(JsonValue.obj("4" -> JsonValue.fromInt(5)))
		)
	}

	//------------------------------------------------------------------------------

	case object X
	implicit val XReader:JsonReader[X.type]	= Converter.total(_ => X)
	implicit val XWriter:JsonWriter[X.type]	= Converter.total(_ => JsonValue.Null)

	test("keyless maps should serialize without having a key writer") {
		assertEquals(
			JsonWriter[Map[X.type,String]] convert Map(X -> "1"),
			Validated.valid(JsonValue.arr(JsonValue.arr(JsonValue.Null, JsonValue.fromString("1"))))
		)
	}

	test("keyless maps should deserialize without having a key reader") {
		assertEquals(
			JsonReader[Map[X.type,String]] convert JsonValue.arr(JsonValue.arr(JsonValue.Null, JsonValue.fromString("1"))),
			Validated.valid(Map(X -> "1"))
		)
	}

//------------------------------------------------------------------------------

	test("tuples should serialize 2-tuples") {
		assertEquals(
			JsonWriter[(String,Int)] convert (("a", 1)),
			Validated.valid(JsonValue.arr(JsonValue.fromString("a"), JsonValue.fromInt(1)))
		)
	}
	test("tuples should serialize 3-tuples") {
		assertEquals(
			JsonWriter[(String,Int,Boolean)] convert (("a", 1, true)),
			Validated.valid(JsonValue.arr(JsonValue.fromString("a"), JsonValue.fromInt(1), JsonValue.True))
		)
	}
	test("tuples should parse 2-tuples") {
		assertEquals(
			JsonReader[(String,Int)] convert JsonValue.arr(JsonValue.fromString("a"), JsonValue.fromInt(1)),
			Validated.valid(("a", 1))
		)
	}
	test("tuples should parse 3-tuples") {
		assertEquals(
			JsonReader[(String,Int,Boolean)] convert JsonValue.arr(JsonValue.fromString("a"), JsonValue.fromInt(1), JsonValue.True),
			Validated.valid(("a", 1, true))
		)
	}

	//------------------------------------------------------------------------------

	/*
	test("keyed tuples should serialize 2-tuples") {
		assertEquals(
			JsonWriter[(String,Int)] convert (("a", 1)),
			Validated.valid(JsonValue.obj("1" -> JsonValue.fromString("a"), "2" -> JsonValue.fromInt(1)))
		)
	}
	test("keyed tuples should serialize 3-tuples") {
		assertEquals(
			JsonWriter[(String,Int,Boolean)] convert (("a", 1, true)),
			Validated.valid(JsonValue.obj("1" -> JsonValue.fromString("a"), "2" -> JsonValue.fromInt(1), "3" -> JsonValue.True))
		)
	}
	test("keyed tuples should parse 2-tuples") {
		assertEquals(
			JsonReader[(String,Int)] convert JsonValue.obj("1" -> JsonValue.fromString("a"), "2" -> JsonValue.fromInt(1)),
			Validated.valid(("a", 1))
		)
	}
	test("keyed tuples should parse 3-tuples") {
		assertEquals(
			JsonReader[(String,Int,Boolean)] convert JsonValue.obj("1" -> JsonValue.fromString("a"), "2" -> JsonValue.fromInt(1), "3" -> JsonValue.True),
			Validated.valid(("a", 1, true))
		)
	}
	*/
}
