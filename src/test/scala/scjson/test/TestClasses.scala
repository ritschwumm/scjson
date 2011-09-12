package scjson.test

case class SimpleCaseClass(ok:Boolean)
case object SimpleCaseObject

sealed abstract class ParamBaseClass(val id:String)
case class InheritingParamCaseClass(val name:String)	extends ParamBaseClass("4711")
case object InheritingParamCaseObject					extends ParamBaseClass("4711")

sealed abstract class FieldBaseClass {
	val	id	= "4711"
}
case class InheritingFieldCaseClass(val name:String)	extends FieldBaseClass
case object InheritingFieldCaseObject					extends FieldBaseClass

// TODO check more inheritance using a private val and data

case class WeirdNameCaseClass(val `*`:Int)

//------------------------------------------------------------------------------

object Outer {
	import Outers._
	
	val all:Seq[Outer] = Seq(
		aborigines
	)
}

sealed abstract class Outer(val id:String)

object Outers {
	case object aborigines		extends Outer("aborigines")
}

//------------------------------------------------------------------------------

object Whip {
	case class Foo(str:String) {
		case class Bar(bong:String)
	}
}
