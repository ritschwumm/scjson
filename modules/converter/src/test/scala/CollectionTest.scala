package scjson.converter

import minitest._

import scutil.lang._

import scjson.ast._

import JsonFormat._

object CollectionTest extends SimpleTestSuite {
	test("special collections should serialize string maps") {
		assertEquals(
			JsonWriter[Map[String,Int]] convert Map("a" -> 1),
			Good(JsonObject.Var("a" -> JsonNumber(1)))
		)
	}
	test("special collections should serialize int maps") {
		assertEquals(
			JsonWriter[Map[Int,Int]] convert Map(2 -> 3),
			Good(JsonObject.Var("2" -> JsonNumber(3)))
		)
	}
	test("special collections should serialize long maps") {
		assertEquals(
			JsonWriter[Map[Long,Int]] convert Map(4L -> 5),
			Good(JsonObject.Var("4" -> JsonNumber(5)))
		)
	}

	//------------------------------------------------------------------------------

	case object X
	implicit val XReader:JsonReader[X.type]	= Converter.total(_ => X)
	implicit val XWriter:JsonWriter[X.type]	= Converter.total(_ => JsonValue.theNull)

	test("keyless maps should serialize without having a key writer") {
		assertEquals(
			JsonWriter[Map[X.type,String]] convert Map(X -> "1"),
			Good(JsonArray.Var(JsonArray.Var(JsonValue.theNull, JsonString("1"))))
		)
	}

	test("keyless maps should deserialize without having a key reader") {
		assertEquals(
			JsonReader[Map[X.type,String]] convert JsonArray.Var(JsonArray.Var(JsonValue.theNull, JsonString("1"))),
			Good(Map(X -> "1"))
		)
	}

//------------------------------------------------------------------------------

	test("tuples should serialize 2-tuples") {
		assertEquals(
			JsonWriter[(String,Int)] convert (("a", 1)),
			Good(JsonArray.Var(JsonString("a"), JsonNumber(1)))
		)
	}
	test("tuples should serialize 3-tuples") {
		assertEquals(
			JsonWriter[(String,Int,Boolean)] convert (("a", 1, true)),
			Good(JsonArray.Var(JsonString("a"), JsonNumber(1), JsonBoolean(true)))
		)
	}
	test("tuples should parse 2-tuples") {
		assertEquals(
			JsonReader[(String,Int)] convert JsonArray.Var(JsonString("a"), JsonNumber(1)),
			Good(("a", 1))
		)
	}
	test("tuples should parse 3-tuples") {
		assertEquals(
			JsonReader[(String,Int,Boolean)] convert JsonArray.Var(JsonString("a"), JsonNumber(1), JsonBoolean(true)),
			Good(("a", 1, true))
		)
	}

	//------------------------------------------------------------------------------

	/*
	test("keyed tuples should serialize 2-tuples") {
		assertEquals(
			JsonWriter[(String,Int)] convert (("a", 1)),
			Good(JsonObject.Var("1" -> JsonString("a"), "2" -> JsonNumber(1)))
		)
	}
	test("keyed tuples should serialize 3-tuples") {
		assertEquals(
			JsonWriter[(String,Int,Boolean)] convert (("a", 1, true)),
			Good(JsonObject.Var("1" -> JsonString("a"), "2" -> JsonNumber(1), "3" -> JsonBoolean(true)))
		)
	}
	test("keyed tuples should parse 2-tuples") {
		assertEquals(
			JsonReader[(String,Int)] convert JsonObject.Var("1" -> JsonString("a"), "2" -> JsonNumber(1)),
			Good(("a", 1))
		)
	}
	test("keyed tuples should parse 3-tuples") {
		assertEquals(
			JsonReader[(String,Int,Boolean)] convert JsonObject.Var("1" -> JsonString("a"), "2" -> JsonNumber(1), "3" -> JsonBoolean(true)),
			Good(("a", 1, true))
		)
	}
	*/
}
