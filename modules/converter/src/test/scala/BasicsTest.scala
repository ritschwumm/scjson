package scjson.converter

import minitest._

import scutil.lang._
import scutil.lang.implicits._

import scjson.ast._
import scjson.codec._

import JsonFormat._

object BasicsTest extends SimpleTestSuite {
	test("Json values should roundtrip null") {
		val in:JsonValue	= JsonNull
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("Json values should roundtrip true") {
		val in:JsonValue	= JsonBoolean(true)
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("Json values should roundtrip false") {
		val in:JsonValue	= JsonBoolean(false)
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("Json values should roundtrip 1") {
		val in:JsonValue	= JsonNumber(1)
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("Json values should roundtrip 2.3") {
		val in:JsonValue	= JsonNumber(2.3)
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("Json values should roundtrip 4.5e6") {
		val in:JsonValue	= JsonNumber(4.5e6)
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("Json values should roundtrip a string") {
		val in:JsonValue	= JsonString("hallo")
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("Json values should roundtrip an array") {
		val in:JsonValue	= JsonArray(Vector[JsonValue](JsonString("b"), JsonNumber(1)))
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("Json values should roundtrip an object") {
		val in:JsonValue	= JsonObject(Vector[(String,JsonValue)]("a" -> JsonString("b"), "c" -> JsonNumber(1)))
		val str:String		= pickle(in)	getOrError "oops"
		val out:JsonValue	= unpickle(str)	getOrError "oops"
		assertEquals(out, in)
	}

	//------------------------------------------------------------------------------

	test("scala values should roundtrip true") {
		type T	= Boolean
		val in:T		= true
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip false") {
		type T	= Boolean
		val in:T		= false
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip 1") {
		type T	= Int
		val in:T		= 1
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip 2.3") {
		type T	= Double
		val in:T		= 2.3
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip 4L") {
		type T	= Long
		val in:T		= 4
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip a string") {
		type T	= String
		val in:T		= "hallo"
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip a Seq") {
		type T	= Seq[Int]
		val in:T		= Vector(1)
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip a string-keyed Map") {
		type T	= Map[String,Int]
		val in:T		= Map("a" -> 1, "b" -> 2)
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip a Map") {
		type T	= Map[Long,Int]
		val in:T		= Map(1L -> 1, 1L -> 2)
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip Unit") {
		type T	= Unit
		val in:T		= ()
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip None") {
		type T	= Option[Int]
		val in:T		= None
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip Some") {
		type T	= Option[Int]
		val in:T		= Some(1)
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip Some[Unit]") {
		type T	= Option[Unit]
		val in:T		= Some(())
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip None[Unit]") {
		type T	= Option[Unit]
		val in:T		= None
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip Some(Some)") {
		type T	= Option[Option[Int]]
		val in:T		= Some(Some(1))
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip Some(None)") {
		type T	= Option[Option[Int]]
		val in:T		= Some(None)
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip None") {
		type T	= Option[Option[Int]]
		val in:T		= None
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip Some(Some(Some))") {
		type T	= Option[Option[Option[Int]]]
		val in:T		= Some(Some(Some(1)))
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip Some(Some(None))") {
		type T	= Option[Option[Option[Int]]]
		val in:T		= Some(Some(None))
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip Some(None)") {
		type T	= Option[Option[Option[Int]]]
		val in:T		= Some(None)
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}
	test("scala values should roundtrip None") {
		type T	= Option[Option[Option[Int]]]
		val in:T		= None
		val str:String	= pickle[T](in)		getOrError "oops"
		val out:T		= unpickle[T](str)	getOrError "oops"
		assertEquals(out, in)
	}

	//------------------------------------------------------------------------------

	val Empty	= JsonObject.Var()

	private def pickle[T:JsonWriter](it:T):JsonResult[String]	=
		JsonWriter[T] convert it flatMap encode

	private def unpickle[T:JsonReader](it:String):JsonResult[T]	=
		decode(it) flatMap JsonReader[T].convert

	private def encode(it:JsonValue):JsonResult[String]	=
		Good(JsonCodec encodeShort it)

	private def decode(it:String):JsonResult[JsonValue]	=
		(JsonCodec decode it leftMap { it => Nes single it.message }).toValidated
}
