package scjson.converter

import org.specs2.mutable._

import scutil.lang._

import scjson.ast._

import JsonFormat._

class CollectionTest extends Specification {
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
	"keyless maps" should {
		case object X
		implicit val XReader:JsonReader[X.type]	= Converter.total(_ => X)
		implicit val XWriter:JsonWriter[X.type]	= Converter.total(_ => JsonValue.theNull)

		"serialize without having a key writer" in {
			JsonWriter[Map[X.type,String]] convert Map(X -> "1") mustEqual
			Good(JsonArray.Var(JsonArray.Var(JsonValue.theNull, JsonString("1"))))
		}

		"deserialize without having a key reader" in {
			JsonReader[Map[X.type,String]] convert JsonArray.Var(JsonArray.Var(JsonValue.theNull, JsonString("1"))) mustEqual
			Good(Map(X -> "1"))
		}
	}
	"tuples should" should {
		"serialize 2-tuples" in {
			JsonWriter[(String,Int)] convert (("a", 1)) mustEqual
			Good(JsonArray.Var(JsonString("a"), JsonNumber(1)))
		}
		"serialize 3-tuples" in {
			JsonWriter[(String,Int,Boolean)] convert (("a", 1, true)) mustEqual
			Good(JsonArray.Var(JsonString("a"), JsonNumber(1), JsonBoolean(true)))
		}
		"parse 2-tuples" in {
			JsonReader[(String,Int)] convert JsonArray.Var(JsonString("a"), JsonNumber(1)) mustEqual
			Good(("a", 1))
		}
		"parse 3-tuples" in {
			JsonReader[(String,Int,Boolean)] convert JsonArray.Var(JsonString("a"), JsonNumber(1), JsonBoolean(true)) mustEqual
			Good(("a", 1, true))
		}
	}
	/*
	"keyed tuples should" should {
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
	*/

}
