package scjson.codec

import minitest.*

import scjson.ast.*

object JsonEncoderTest extends SimpleTestSuite {
	test("JsonEncoder should encode null") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.Null),
			"null"
		)
	}
	test("JsonEncoder should encode true") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.True),
			"true"
		)
	}
	test("JsonEncoder should encode false") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.False),
			"false"
		)
	}

	test("JsonEncoder should encode int 0") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.Zero),
			"0"
		)
	}
	test("JsonEncoder should encode int -1") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromInt(-1)),
			"-1"
		)
	}
	test("JsonEncoder should encode int 1") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromInt(1)),
			"1"
		)
	}

	test("JsonEncoder should encode float 0.0") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromFloat(0f)),
			"0.0"
		)
	}
	test("JsonEncoder should encode float 1e1") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromFloat(1e1f)),
			"10.0"
		)
	}
	test("JsonEncoder should encode float 2e+3") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromFloat(2e+3f)),
			"2000.0"
		)
	}
	test("JsonEncoder should encode float 10e-1") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromFloat(10e-1f)),
			"1.0"
		)
	}

	/*
	// TODO check this
	test("JsonEncoder should encode float 47.11") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromFloat(47.11f)),
			"47.11"
		)
	}
	*/

	test("JsonEncoder should encode simple strings") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromString("hallo, welt!")),
			"\"hallo, welt!\""
		)
	}
	test("JsonEncoder should encode string escapes") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromString(" \\ \t \r \n \f \b ")),
			"\" \\\\ \\t \\r \\n \\f \\b \""
		)
	}
	test("JsonEncoder should encode small hex escapes") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromString("\u0011")),
			"\"\\u0011\""
		)
	}
	/*
	test("JsonEncoder should encode big hex escapes") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.fromString("\ufe12")),
			"\"\\ufe12\""
		)
	}
	*/

	test("JsonEncoder should encode arrays with 0 elements") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.emptyArray),
			"[]"
		)
	}
	test("JsonEncoder should encode arrays with 1 element") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.arr(JsonValue.fromInt(1))),
			"[1]"
		)
	}
	test("JsonEncoder should encode arrays with 2 elements") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.arr(JsonValue.fromInt(1), JsonValue.fromInt(2))),
			"[1,2]"
		)
	}

	test("JsonEncoder should encode objects with 0 elements") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.emptyObject),
			"{}"
		)
	}
	test("JsonEncoder should encode objects with 1 element") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.obj("a" -> JsonValue.fromInt(1))),
			"{\"a\":1}"
		)
	}
	test("JsonEncoder should encode objects with 2 elements") {
		assertEquals(
			JsonCodec.encodeShort(JsonValue.obj("a" -> JsonValue.fromInt(1), "b" -> JsonValue.fromInt(2))),
			"{\"a\":1,\"b\":2}"
		)
	}
}
