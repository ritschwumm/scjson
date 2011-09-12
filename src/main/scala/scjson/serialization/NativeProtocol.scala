package scjson.serialization

import scjson._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit object UnitFormat extends Format[Unit] {
		def write(out:Unit):JSValue	= JSObject(Map.empty)
		def read(in:JSValue):Unit	= ()
	}
	
	implicit object NullFormat extends Format[Null] {
		def write(out:Null):JSValue	= JSNull
		def read(in:JSValue):Null	= null
	}

	implicit object ByteFormat extends Format[Byte] {
		def write(out:Byte):JSValue	= JSNumber(out)
		def read(in:JSValue):Byte	= in.asInstanceOf[JSNumber].value.toByte
	}
	implicit object ShortFormat extends Format[Short] {
		def write(out:Short):JSValue	= JSNumber(out)
		def read(in:JSValue):Short		= in.asInstanceOf[JSNumber].value.toShort
	}
	implicit object IntFormat extends Format[Int] {
		def write(out:Int):JSValue		= JSNumber(out)
		def read(in:JSValue):Int		= in.asInstanceOf[JSNumber].value.toInt
	}
	implicit object LongFormat extends Format[Long] {
		def write(out:Long):JSValue		= JSNumber(out)
		def read(in:JSValue):Long		= in.asInstanceOf[JSNumber].value.toLong
	} 
	implicit object FloatFormat extends Format[Float] {
		def write(out:Float):JSValue	= JSNumber(out)
		def read(in:JSValue):Float		= in.asInstanceOf[JSNumber].value.toFloat
	}
	implicit object DoubleFormat extends Format[Double] {
		def write(out:Double):JSValue	= JSNumber(out)
		def read(in:JSValue):Double		= in.asInstanceOf[JSNumber].value.toDouble
	}
	
	implicit object BigIntFormat extends Format[BigInt] {
		def write(out:BigInt):JSValue		= JSNumber(out)
		def read(in:JSValue):BigInt			= in.asInstanceOf[JSNumber].value.toBigInt
	}
	implicit object BigDecimalFormat  extends Format[BigDecimal] {
		def write(out:BigDecimal):JSValue	= JSNumber(out)
		def read(in:JSValue):BigDecimal		= in.asInstanceOf[JSNumber].value
	}
	
	implicit object BooleanFormat extends Format[Boolean] {
		def write(out:Boolean):JSValue	= out match {
			case true	=> JSTrue
			case false	=> JSFalse
		}
		def read(in:JSValue):Boolean	= in.asInstanceOf[JSBoolean] match {
			case JSTrue		=> true
			case JSFalse	=> false
		}
	}
	
	implicit object StringFormat extends Format[String] {
		def write(out:String):JSValue	= JSString(out)
		def read(in:JSValue):String		= in.asInstanceOf[JSString].value
	}
}
