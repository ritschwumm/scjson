package scjson.converter

import minitest._

import scutil.lang._

import scjson.ast._

import JsonFormat._

//------------------------------------------------------------------------------

object TestEnum {
	case object X1	extends TestEnum
	case object X2	extends TestEnum
	case object X3	extends TestEnum

	implicit val TestEnumReader:JsonReader[TestEnum]	=
		enumReaderPf {
			case "x1"	=> X1
			case "x2"	=> X2
			case "x3"	=> X3
		}
	implicit val TestEnumWriter:JsonWriter[TestEnum]	=
		enumWriter {
			case X1	=>	"x1"
			case X2	=>	"x2"
			case X3	=>	"x3"
		}
}
sealed trait TestEnum

//------------------------------------------------------------------------------

object EnumTest extends SimpleTestSuite {
	test("enums should unparse ") {
		assertEquals(
			JsonWriter[TestEnum] convert TestEnum.X1,
			Validated.good(JsonString("x1"))
		)
	}

	test("enums should parse") {
		assertEquals(
			JsonReader[TestEnum] convert JsonString("x2"),
			Validated.good(TestEnum.X2)
		)
	}
}
