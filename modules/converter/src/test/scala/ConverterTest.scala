package scjson.converter

import org.specs2.mutable._

import scutil.lang._
import scutil.lang.implicits._

import scjson.ast._
import scjson.codec._

import JsonFormat._

class ConverterTest extends Specification {
	"Json values" should {
		"roundtrip null" in {
			val in:JsonValue	= JsonNull
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip true" in {
			val in:JsonValue	= JsonBoolean(true)
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip false" in {
			val in:JsonValue	= JsonBoolean(false)
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip 1" in {
			val in:JsonValue	= JsonNumber(1)
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip 2.3" in {
			val in:JsonValue	= JsonNumber(2.3)
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip 4.5e6" in {
			val in:JsonValue	= JsonNumber(4.5e6)
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip a string" in {
			val in:JsonValue	= JsonString("hallo")
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip an array" in {
			val in:JsonValue	= JsonArray(Vector(JsonString("b"), JsonNumber(1)))
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip an object" in {
			val in:JsonValue	= JsonObject(Vector("a" -> JsonString("b"), "c" -> JsonNumber(1)))
			val str:String		= pickle(in)	getOrError "oops"
			val out:JsonValue	= unpickle(str)	getOrError "oops"
			out mustEqual in
		}
	}
	"scala values" should {
		"roundtrip true" in {
			type T	= Boolean
			val in:T		= true
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip false" in {
			type T	= Boolean
			val in:T		= false
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip 1" in {
			type T	= Int
			val in:T		= 1
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip 2.3" in {
			type T	= Double
			val in:T		= 2.3
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip 4L" in {
			type T	= Long
			val in:T		= 4
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip a string" in {
			type T	= String
			val in:T		= "hallo"
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip an ISeq" in {
			type T	= ISeq[Int]
			val in:T		= Vector(1)
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip a string-keyed Map" in {
			type T	= Map[String,Int]
			val in:T		= Map("a" -> 1, "b" -> 2)
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip a Map" in {
			type T	= Map[Long,Int]
			val in:T		= Map(1L -> 1, 1L -> 2)
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip Unit" in {
			type T	= Unit
			val in:T		= ()
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip None" in {
			type T	= Option[Int]
			val in:T		= None
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip Some" in {
			type T	= Option[Int]
			val in:T		= Some(1)
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip Some[Unit]" in {
			type T	= Option[Unit]
			val in:T		= Some(())
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip None[Unit]" in {
			type T	= Option[Unit]
			val in:T		= None
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip Some(Some)" in {
			type T	= Option[Option[Int]]
			val in:T		= Some(Some(1))
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip Some(None)" in {
			type T	= Option[Option[Int]]
			val in:T		= Some(None)
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip None" in {
			type T	= Option[Option[Int]]
			val in:T		= None
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip Some(Some(Some))" in {
			type T	= Option[Option[Option[Int]]]
			val in:T		= Some(Some(Some(1)))
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip Some(Some(None))" in {
			type T	= Option[Option[Option[Int]]]
			val in:T		= Some(Some(None))
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip Some(None)" in {
			type T	= Option[Option[Option[Int]]]
			val in:T		= Some(None)
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
		"roundtrip None" in {
			type T	= Option[Option[Option[Int]]]
			val in:T		= None
			val str:String	= pickle[T](in)		getOrError "oops"
			val out:T		= unpickle[T](str)	getOrError "oops"
			out mustEqual in
		}
	}
	"special collections" should {
		"serialize string maps" in {
			JsonWriter[Map[String,Int]] convert Map("a" -> 1) mustEqual
			Good(JsonObject.Var("a" -> JsonNumber(1)))
		}
		"serialize int maps" in {
			JsonWriter[Map[Int,Int]] convert Map(2 -> 3) mustEqual
			Good(JsonObject.Var("2" -> JsonNumber(3)))
		}
		"serialize long maps" in {
			JsonWriter[Map[Long,Int]] convert Map(4L -> 5) mustEqual
			Good(JsonObject.Var("4" -> JsonNumber(5)))
		}
	}
	"tuples should" should {
		"serialize 2-tuples" in {
			JsonWriter[(String,Int)] convert (("a", 1)) mustEqual
			Good(JsonObject.Var("1" -> JsonString("a"), "2" -> JsonNumber(1)))
		}
		"serialize 3-tuples" in {
			JsonWriter[(String,Int,Boolean)] convert (("a", 1, true)) mustEqual
			Good(JsonObject.Var("1" -> JsonString("a"), "2" -> JsonNumber(1), "3" -> JsonBoolean(true)))
		}
		"parse 2-tuples" in {
			JsonReader[(String,Int)] convert JsonObject.Var("1" -> JsonString("a"), "2" -> JsonNumber(1)) mustEqual
			Good(("a", 1))
		}
		"parse 3-tuples" in {
			JsonReader[(String,Int,Boolean)] convert JsonObject.Var("1" -> JsonString("a"), "2" -> JsonNumber(1), "3" -> JsonBoolean(true)) mustEqual
			Good(("a", 1, true))
		}
	}

	//------------------------------------------------------------------------------

	private def pickle[T:JsonWriter](it:T):JsonResult[String]	=
			JsonWriter[T] convert it flatMap encode

	private def unpickle[T:JsonReader](it:String):JsonResult[T]	=
			decode(it) flatMap JsonReader[T].convert

	private def encode(it:JsonValue):JsonResult[String]	=
			Good(JsonCodec encodeShort it)

	private def decode(it:String):JsonResult[JsonValue]	=
			(JsonCodec decode it leftMap { it => Nes single it.message }).toValidated
}
