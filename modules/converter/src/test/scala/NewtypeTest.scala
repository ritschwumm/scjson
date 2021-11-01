package scjson.converter

import minitest._

import scutil.lang._

import scjson.ast._

import JsonFormat._

final case class NewtypeFixed(value:String)
final case class NewtypeGeneric[T](value:T)

object NewtypeTest extends SimpleTestSuite {
	implicit val NewtypeFixedReader:JsonReader[NewtypeFixed]	= newtypeReader(NewtypeFixed.apply)
	implicit val NewtypeFixedWriter:JsonWriter[NewtypeFixed]	= newtypeWriter(NewtypeFixed.unapply)

	test("fixed newtypes should unparse") {
		assertEquals(
			JsonWriter[NewtypeFixed] convert NewtypeFixed("test"),
			Validated.valid(JsonValue.fromString("test"))
		)
	}

	test("fixed newtypes should parse") {
		assertEquals(
			JsonReader[NewtypeFixed] convert JsonValue.fromString("test"),
			Validated.valid(NewtypeFixed("test"))
		)
	}

	//------------------------------------------------------------------------------

	implicit def NewtypeGenericReader[T:JsonReader]:JsonReader[NewtypeGeneric[T]]	= newtypeReader(NewtypeGeneric.apply)
	implicit def NewtypeGenericWriter[T:JsonWriter]:JsonWriter[NewtypeGeneric[T]]	= newtypeWriter(NewtypeGeneric.unapply)

	test("generic newtypes should unparse") {
		assertEquals(
			JsonWriter[NewtypeGeneric[Int]] convert NewtypeGeneric(4711),
			Validated.valid(JsonValue.fromInt(4711))
		)
	}

	test("generic newtypes should parse") {
		assertEquals(
			JsonReader[NewtypeGeneric[Int]] convert JsonValue.fromInt(4711),
			Validated.valid(NewtypeGeneric(4711))
		)
	}
}
