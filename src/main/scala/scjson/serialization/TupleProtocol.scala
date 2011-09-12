package scjson.serialization

import scjson._

import Operations._

object TupleProtocol extends TupleProtocol

trait TupleProtocol {
	implicit def Tuple2Format[T1:Format,T2:Format]:Format[(T1,T2)]	= new Format[(T1,T2)] {
		def write(out:(T1,T2)):JSValue	= {
			JSArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2)
			))
		}
		def read(in:JSValue):(T1,T2)	= {
			val	arr	= in.asInstanceOf[JSArray].value
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1))
			)
		}
	}
	
	implicit def Tuple3Format[T1:Format,T2:Format,T3:Format]:Format[(T1,T2,T3)]	= new Format[(T1,T2,T3)] {
		def write(out:(T1,T2,T3)):JSValue	= {
			JSArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2),
				doWrite[T3](out._3)
			))
		}
		def read(in:JSValue):(T1,T2,T3)	= {
			val	arr	= in.asInstanceOf[JSArray].value
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1)),
				doRead[T3](arr(2))
			)
		}
	}
	
	implicit def Tuple4Format[T1:Format,T2:Format,T3:Format,T4:Format]:Format[(T1,T2,T3,T4)]	= new Format[(T1,T2,T3,T4)] {
		def write(out:(T1,T2,T3,T4)):JSValue	= {
			JSArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2),
				doWrite[T3](out._3),
				doWrite[T4](out._4)
			))
		}
		def read(in:JSValue):(T1,T2,T3,T4)	= {
			val	arr	= in.asInstanceOf[JSArray].value
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1)),
				doRead[T3](arr(2)),
				doRead[T4](arr(3))
			)
		}
	}
	
	implicit def Tuple5Format[T1:Format,T2:Format,T3:Format,T4:Format,T5:Format]:Format[(T1,T2,T3,T4,T5)]	= new Format[(T1,T2,T3,T4,T5)] {
		def read(in:JSValue):(T1,T2,T3,T4,T5)	= {
			val	arr	= in.asInstanceOf[JSArray].value
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1)),
				doRead[T3](arr(2)),
				doRead[T4](arr(3)),
				doRead[T5](arr(4))
			)
		}
		def write(out:(T1,T2,T3,T4,T5)):JSValue	= {
			JSArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2),
				doWrite[T3](out._3),
				doWrite[T4](out._4),
				doWrite[T5](out._5)
			))
		}
	}
	
	implicit def Tuple6Format[T1:Format,T2:Format,T3:Format,T4:Format,T5:Format,T6:Format]:Format[(T1,T2,T3,T4,T5,T6)]	= new Format[(T1,T2,T3,T4,T5,T6)] {
		def read(in:JSValue):(T1,T2,T3,T4,T5,T6)	= {
			val	arr	= in.asInstanceOf[JSArray].value
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1)),
				doRead[T3](arr(2)),
				doRead[T4](arr(3)),
				doRead[T5](arr(4)),
				doRead[T6](arr(5))
			)
		}
		def write(out:(T1,T2,T3,T4,T5,T6)):JSValue	= {
			JSArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2),
				doWrite[T3](out._3),
				doWrite[T4](out._4),
				doWrite[T5](out._5),
				doWrite[T6](out._6)
			))
		}
	}
	
	implicit def Tuple7Format[T1:Format,T2:Format,T3:Format,T4:Format,T5:Format,T6:Format,T7:Format]:Format[(T1,T2,T3,T4,T5,T6,T7)]	= new Format[(T1,T2,T3,T4,T5,T6,T7)] {
		def read(in:JSValue):(T1,T2,T3,T4,T5,T6,T7)	= {
			val	arr	= in.asInstanceOf[JSArray].value
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1)),
				doRead[T3](arr(2)),
				doRead[T4](arr(3)),
				doRead[T5](arr(4)),
				doRead[T6](arr(5)),
				doRead[T7](arr(6))
			)
		}
		def write(out:(T1,T2,T3,T4,T5,T6,T7)):JSValue	= {
			JSArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2),
				doWrite[T3](out._3),
				doWrite[T4](out._4),
				doWrite[T5](out._5),
				doWrite[T6](out._6),
				doWrite[T7](out._7)
			))
		}
	}
	
	implicit def Tuple8Format[T1:Format,T2:Format,T3:Format,T4:Format,T5:Format,T6:Format,T7:Format,T8:Format]:Format[(T1,T2,T3,T4,T5,T6,T7,T8)]	= new Format[(T1,T2,T3,T4,T5,T6,T7,T8)] {
		def read(in:JSValue):(T1,T2,T3,T4,T5,T6,T7,T8)	= {
			val	arr	= in.asInstanceOf[JSArray].value
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1)),
				doRead[T3](arr(2)),
				doRead[T4](arr(3)),
				doRead[T5](arr(4)),
				doRead[T6](arr(5)),
				doRead[T7](arr(6)),
				doRead[T8](arr(7))
			)
		}
		def write(out:(T1,T2,T3,T4,T5,T6,T7,T8)):JSValue	= {
			JSArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2),
				doWrite[T3](out._3),
				doWrite[T4](out._4),
				doWrite[T5](out._5),
				doWrite[T6](out._6),
				doWrite[T7](out._7),
				doWrite[T8](out._8)
			))
		}
	}
	
	implicit def Tuple9Format[T1:Format,T2:Format,T3:Format,T4:Format,T5:Format,T6:Format,T7:Format,T8:Format,T9:Format]:Format[(T1,T2,T3,T4,T5,T6,T7,T8,T9)]	= new Format[(T1,T2,T3,T4,T5,T6,T7,T8,T9)] {
		def read(in:JSValue):(T1,T2,T3,T4,T5,T6,T7,T8,T9)	= {
			val	arr	= in.asInstanceOf[JSArray].value
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1)),
				doRead[T3](arr(2)),
				doRead[T4](arr(3)),
				doRead[T5](arr(4)),
				doRead[T6](arr(5)),
				doRead[T7](arr(6)),
				doRead[T8](arr(7)),
				doRead[T9](arr(8))
			)
		}
		def write(out:(T1,T2,T3,T4,T5,T6,T7,T8,T9)):JSValue	= {
			JSArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2),
				doWrite[T3](out._3),
				doWrite[T4](out._4),
				doWrite[T5](out._5),
				doWrite[T6](out._6),
				doWrite[T7](out._7),
				doWrite[T8](out._8),
				doWrite[T9](out._9)
			))
		}
	}
}
