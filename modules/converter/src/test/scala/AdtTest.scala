package scjson.converter

import org.specs2.mutable._

import scutil.lang._

import scjson.ast._
import scjson.converter.{ SumConverters => SC }

import JsonFormat._

//------------------------------------------------------------------------------

object TestAdt {
	case object TestAdt0						extends TestAdt
	final case class TestAdt1(a:Int)			extends TestAdt
	final case class TestAdt2(a:Int, b:String)	extends TestAdt

	object P {
		type Self	= TestAdt
		val Self	= TestAdt
		val TestAdt0	= Prism.Gen[Self,Self.TestAdt0.type]
		val TestAdt1	= Prism.Gen[Self,Self.TestAdt1]
		val TestAdt2	= Prism.Gen[Self,Self.TestAdt2]
	}

	implicit val TestAdtReader:JsonReader[TestAdt]	=
		SC sumReader Seq(
			"TestAdt0"		-> SC.subReader(coReader(TestAdt0)),
			"TestAdt1"		-> SC.subReader(cc1AutoReader(TestAdt1.apply)),
			"TestAdt2"		-> SC.subReader(cc2AutoReader(TestAdt2.apply))
		)
	implicit val TestAdtWriter:JsonWriter[TestAdt]	=
		SC sumWriter Seq(
			"TestAdt0"		-> SC.subWriter(coWriter(TestAdt0),					P.TestAdt0.get),
			"TestAdt1"		-> SC.subWriter(cc1AutoWriter(TestAdt1.unapply),	P.TestAdt1.get),
			"TestAdt2"		-> SC.subWriter(cc2AutoWriter(TestAdt2.unapply),	P.TestAdt2.get)
		)
}

sealed trait TestAdt

//------------------------------------------------------------------------------

class AdtTest extends Specification {
	val Empty	= JsonObject.Var()

	"adts should" should {
		"unparse 0" in {
			JsonWriter[TestAdt] convert TestAdt.TestAdt0 mustEqual
			Good(JsonObject.Var("TestAdt0" -> Empty))
		}
		"parse 0" in {
			JsonReader[TestAdt] convert JsonObject.Var("TestAdt0" -> Empty) mustEqual
			Good(TestAdt.TestAdt0)
		}

		"unparse 1" in {
			JsonWriter[TestAdt] convert TestAdt.TestAdt1(7) mustEqual
			Good(JsonObject.Var("TestAdt1" -> JsonObject.Var("a" -> JsonNumber(7))))
		}
		"parse 1" in {
			JsonReader[TestAdt] convert JsonObject.Var("TestAdt1" -> JsonObject.Var("a" -> JsonNumber(7))) mustEqual
			Good(TestAdt.TestAdt1(7))
		}

		"unparse 2" in {
			JsonWriter[TestAdt] convert TestAdt.TestAdt2(7, "z") mustEqual
			Good(JsonObject.Var("TestAdt2" -> JsonObject.Var("a" -> JsonNumber(7), "b" -> JsonString("z"))))
		}
		"parse 2" in {
			JsonReader[TestAdt] convert JsonObject.Var("TestAdt2" -> JsonObject.Var("a" -> JsonNumber(7), "b" -> JsonString("z"))) mustEqual
			Good(TestAdt.TestAdt2(7, "z"))
		}
	}
}
