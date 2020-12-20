package scjson.codec

import minitest._

import scjson.ast._

object JsonDecoderTest extends SimpleTestSuite {
	test("JsonDecoder should decode null") {
		assertEquals(
			JsonCodec decode "null",
			Right(JsonNull)
		)
	}
	test("JsonDecoder should decode true") {
		assertEquals(
			JsonCodec decode "true",
			Right(JsonTrue)
		)
	}
	test("JsonDecoder should decode false") {
		assertEquals(
			JsonCodec decode "false",
			Right(JsonFalse)
		)
	}

	test("JsonDecoder should decode int 0") {
		assertEquals(
			JsonCodec decode "0",
			Right(JsonNumber(0))
		)
	}
	test("JsonDecoder should decode int -1") {
		assertEquals(
			JsonCodec decode "-1",
			Right(JsonNumber(-1))
		)
	}
	test("JsonDecoder should decode int 1") {
		assertEquals(
			JsonCodec decode "1",
			Right(JsonNumber(1))
		)
	}

	test("JsonDecoder should fail with a single dot") {
		assert(
			(JsonCodec decode ".").isLeft
		)
	}
	test("JsonDecoder should fail without fraction after dot") {
		assert(
			(JsonCodec decode "0.").isLeft
		)
	}
	test("JsonDecoder should fail without int before fraction") {
		assert(
			(JsonCodec decode ".0").isLeft
		)
	}
	test("JsonDecoder should fail with a single dot before exp") {
		assert(
			(JsonCodec decode ".e1").isLeft
		)
	}
	test("JsonDecoder should fail without int before exp") {
		assert(
			(JsonCodec decode "e1").isLeft
		)
	}
	test("JsonDecoder should fail without digits after exp") {
		assert(
			(JsonCodec decode "1e").isLeft
		)
	}

	test("JsonDecoder should decode float 0.0") {
		assertEquals(
			JsonCodec decode "0.0",
			Right(JsonNumber(0))
		)
	}
	test("JsonDecoder should decode float 1e1") {
		assertEquals(
			JsonCodec decode "1e1",
			Right(JsonNumber(10))
		)
	}
	test("JsonDecoder should decode float 2e+3") {
		assertEquals(
			JsonCodec decode "2e+3",
			Right(JsonNumber(2000))
		)
	}
	test("JsonDecoder should decode float 10e-1") {
		assertEquals(
			JsonCodec decode "10e-1",
			Right(JsonNumber(1))
		)
	}
	test("JsonDecoder should decode float 47.11") {
		assertEquals(
			JsonCodec decode "47.11",
			Right(JsonNumber(47.11))
		)
	}

	test("JsonDecoder should fail with a leading zero in the body") {
		// (JsonCodec decode "00") must beLike { case Fail(_) => ok }
		assert(
			(JsonCodec decode "00").isLeft
		)
	}
	test("JsonDecoder should allow a leading zero in the exponent") {
		assertEquals(
			JsonCodec decode "0E00",
			Right(JsonNumber(0))
		)
	}

	test("JsonDecoder should decode simple strings") {
		assertEquals(
			JsonCodec decode "\"hallo, welt!\"",
			Right(JsonString("hallo, welt!"))
		)
	}
	test("JsonDecoder should decode string escapes") {
		assertEquals(
			JsonCodec decode "\" \\\\ \\/ \\t \\r \\n \\f \\b \"",
			Right(JsonString(" \\ / \t \r \n \f \b "))
		)
	}
	test("JsonDecoder should decode small hex escapes") {
		assertEquals(
			JsonCodec decode "\" \\u0123 \"",
			Right(JsonString(" \u0123 "))
		)
	}
	test("JsonDecoder should decode big hex escapes") {
		assertEquals(
			JsonCodec decode "\" \\uf3e2 \"",
			Right(JsonString(" \uf3e2 "))
		)
	}
	test("JsonDecoder should decode upper case hex escapes") {
		assertEquals(
			JsonCodec decode "\" \\uBEEF \"",
			Right(JsonString(" \uBEEF "))
		)
	}
	test("JsonDecoder should decode hex escapes outside the basic plane") {
		assertEquals(
			JsonCodec decode "\"\\uD834\\uDD1E\"",
			Right(JsonString("\uD834\uDD1E"))
		)
	}
	test("JsonDecoder should decode hex escapes outside the basic plane") {
		val cs	= (new java.lang.StringBuilder appendCodePoint 0x1D11E).toString
		assertEquals(
			JsonCodec decode "\"\\uD834\\uDD1E\"",
			Right(JsonString(cs))
		)
	}

	test("JsonDecoder should decode arrays with 0 elements") {
		assertEquals(
			JsonCodec decode "[]",
			Right(JsonArray(Seq()))
		)
	}
	test("JsonDecoder should decode arrays with 1 elements") {
		assertEquals(
			JsonCodec decode "[1]",
			Right(JsonArray(Seq(JsonNumber(1))))
		)
	}
	test("JsonDecoder should decode arrays with 2 elements") {
		assertEquals(
			JsonCodec decode "[1,2]",
			Right(JsonArray(Seq(JsonNumber(1),JsonNumber(2))))
		)
	}

	test("JsonDecoder should allow legal whitespace in arrays") {
		assertEquals(
			JsonCodec decode "[ ]",
			Right(JsonArray(Seq()))
		)
	}
	test("JsonDecoder should disallow illegal whitespace in arrays") {
		assert(
			(JsonCodec decode "[\u00a0]").isLeft
		)
	}

	test("JsonDecoder should decode objects with 0 elements") {
		assertEquals(
			JsonCodec decode "{}",
			Right(JsonObject.empty)
		)
	}
	test("JsonDecoder should decode objects with 1 elements") {
		assertEquals(
			JsonCodec decode "{\"a\":1}",
			Right(JsonObject(Seq("a"->JsonNumber(1))))
		)
	}
	test("JsonDecoder should decode objects with 2 elements") {
		assertEquals(
			JsonCodec decode "{\"a\":1,\"b\":2}",
			Right(JsonObject(Seq("a"->JsonNumber(1),"b"->JsonNumber(2))))
		)
	}
}
