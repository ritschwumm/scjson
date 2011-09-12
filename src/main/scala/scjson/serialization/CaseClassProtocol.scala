package scjson.serialization

import scjson._
import scjson.reflection._

import Operations._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol {
	//------------------------------------------------------------------------------
	//## flexible variant with configurable type- and parameter names
	
	def caseObjectFormat[T:Manifest](singleton:T):Format[T]	= {
		new Format[T] {
			def write(out:T):JSValue	= {
				JSObject(Map.empty)
			}
			def read(in:JSValue):T	= {
				singleton
			}
		}
	}
	
	def caseClassFormat1[S1:Format,T:Manifest](apply:S1=>T, unapply:T=>Option[S1]):Format[T]	= {
		val Seq(k1)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1)))
				)
			}
		}
	}
	
	def caseClassFormat2[S1:Format,S2:Format,T:Manifest](apply:(S1,S2)=>T, unapply:T=>Option[(S1,S2)]):Format[T]	= {
		val Seq(k1,k2)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields._1),
					JSString(k2)	-> doWrite[S2](fields._2)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1))),
					doRead[S2](map(JSString(k2)))
				)
			}
		}
	}
	
	def caseClassFormat3[S1:Format,S2:Format,S3:Format,T:Manifest](apply:(S1,S2,S3)=>T, unapply:T=>Option[(S1,S2,S3)]):Format[T]	= {
		val Seq(k1,k2,k3)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields._1),
					JSString(k2)	-> doWrite[S2](fields._2),
					JSString(k3)	-> doWrite[S3](fields._3)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1))),
					doRead[S2](map(JSString(k2))),
					doRead[S3](map(JSString(k3)))
				)
			}
		}
	}
	
	def caseClassFormat4[S1:Format,S2:Format,S3:Format,S4:Format,T:Manifest](apply:(S1,S2,S3,S4)=>T, unapply:T=>Option[(S1,S2,S3,S4)]):Format[T]	= {
		val Seq(k1,k2,k3,k4)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields._1),
					JSString(k2)	-> doWrite[S2](fields._2),
					JSString(k3)	-> doWrite[S3](fields._3),
					JSString(k4)	-> doWrite[S4](fields._4)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1))),
					doRead[S2](map(JSString(k2))),
					doRead[S3](map(JSString(k3))),
					doRead[S4](map(JSString(k4)))
				)
			}
		}
	}
		
	def caseClassFormat5[S1:Format,S2:Format,S3:Format,S4:Format,S5:Format,T:Manifest](apply:(S1,S2,S3,S4,S5)=>T, unapply:T=>Option[(S1,S2,S3,S4,S5)]):Format[T]	= {
		val Seq(k1,k2,k3,k4,k5)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields._1),
					JSString(k2)	-> doWrite[S2](fields._2),
					JSString(k3)	-> doWrite[S3](fields._3),
					JSString(k4)	-> doWrite[S4](fields._4),
					JSString(k5)	-> doWrite[S5](fields._5)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1))),
					doRead[S2](map(JSString(k2))),
					doRead[S3](map(JSString(k3))),
					doRead[S4](map(JSString(k4))),
					doRead[S5](map(JSString(k5)))
				)
			}
		}
	}
	
	def caseClassFormat6[S1:Format,S2:Format,S3:Format,S4:Format,S5:Format,S6:Format,T:Manifest](apply:(S1,S2,S3,S4,S5,S6)=>T, unapply:T=>Option[(S1,S2,S3,S4,S5,S6)]):Format[T]	= {
		val Seq(k1,k2,k3,k4,k5,k6)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields._1),
					JSString(k2)	-> doWrite[S2](fields._2),
					JSString(k3)	-> doWrite[S3](fields._3),
					JSString(k4)	-> doWrite[S4](fields._4),
					JSString(k5)	-> doWrite[S5](fields._5),
					JSString(k6)	-> doWrite[S6](fields._6)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1))),
					doRead[S2](map(JSString(k2))),
					doRead[S3](map(JSString(k3))),
					doRead[S4](map(JSString(k4))),
					doRead[S5](map(JSString(k5))),
					doRead[S6](map(JSString(k6)))
				)
			}
		}
	}
	
	def caseClassFormat7[S1:Format,S2:Format,S3:Format,S4:Format,S5:Format,S6:Format,S7:Format,T:Manifest](apply:(S1,S2,S3,S4,S5,S6,S7)=>T, unapply:T=>Option[(S1,S2,S3,S4,S5,S6,S7)]):Format[T]	= {
		val Seq(k1,k2,k3,k4,k5,k6,k7)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields._1),
					JSString(k2)	-> doWrite[S2](fields._2),
					JSString(k3)	-> doWrite[S3](fields._3),
					JSString(k4)	-> doWrite[S4](fields._4),
					JSString(k5)	-> doWrite[S5](fields._5),
					JSString(k6)	-> doWrite[S6](fields._6),
					JSString(k7)	-> doWrite[S7](fields._7)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1))),
					doRead[S2](map(JSString(k2))),
					doRead[S3](map(JSString(k3))),
					doRead[S4](map(JSString(k4))),
					doRead[S5](map(JSString(k5))),
					doRead[S6](map(JSString(k6))),
					doRead[S7](map(JSString(k7)))
				)
			}
		}
	}
	
	def caseClassFormat8[S1:Format,S2:Format,S3:Format,S4:Format,S5:Format,S6:Format,S7:Format,S8:Format,T:Manifest](apply:(S1,S2,S3,S4,S5,S6,S7,S8)=>T, unapply:T=>Option[(S1,S2,S3,S4,S5,S6,S7,S8)]):Format[T]	= {
		val Seq(k1,k2,k3,k4,k5,k6,k7,k8)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields._1),
					JSString(k2)	-> doWrite[S2](fields._2),
					JSString(k3)	-> doWrite[S3](fields._3),
					JSString(k4)	-> doWrite[S4](fields._4),
					JSString(k5)	-> doWrite[S5](fields._5),
					JSString(k6)	-> doWrite[S6](fields._6),
					JSString(k7)	-> doWrite[S7](fields._7),
					JSString(k8)	-> doWrite[S8](fields._8)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1))),
					doRead[S2](map(JSString(k2))),
					doRead[S3](map(JSString(k3))),
					doRead[S4](map(JSString(k4))),
					doRead[S5](map(JSString(k5))),
					doRead[S6](map(JSString(k6))),
					doRead[S7](map(JSString(k7))),
					doRead[S8](map(JSString(k8)))
				)
			}
		}
	}
	
	def caseClassFormat9[S1:Format,S2:Format,S3:Format,S4:Format,S5:Format,S6:Format,S7:Format,S8:Format,S9:Format,T:Manifest](apply:(S1,S2,S3,S4,S5,S6,S7,S8,S9)=>T, unapply:T=>Option[(S1,S2,S3,S4,S5,S6,S7,S8,S9)]):Format[T]	= {
		val Seq(k1,k2,k3,k4,k5,k6,k7,k8,k9)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):JSValue	= {
				val fields	= unapply(out).get
				JSObject(Map(
					JSString(k1)	-> doWrite[S1](fields._1),
					JSString(k2)	-> doWrite[S2](fields._2),
					JSString(k3)	-> doWrite[S3](fields._3),
					JSString(k4)	-> doWrite[S4](fields._4),
					JSString(k5)	-> doWrite[S5](fields._5),
					JSString(k6)	-> doWrite[S6](fields._6),
					JSString(k7)	-> doWrite[S7](fields._7),
					JSString(k8)	-> doWrite[S8](fields._8),
					JSString(k9)	-> doWrite[S9](fields._9)
				))
			}
			def read(in:JSValue):T	= {
				val map	= in.asInstanceOf[JSObject].value
				apply(
					doRead[S1](map(JSString(k1))),
					doRead[S2](map(JSString(k2))),
					doRead[S3](map(JSString(k3))),
					doRead[S4](map(JSString(k4))),
					doRead[S5](map(JSString(k5))),
					doRead[S6](map(JSString(k6))),
					doRead[S7](map(JSString(k7))),
					doRead[S8](map(JSString(k8))),
					doRead[S9](map(JSString(k9)))
				)
			}
		}
	}
	
	// TODO cache results
	private def fieldNamesFor[T:Manifest]:Seq[String]	=
			(Reflector constructor manifest[T].erasure).get
			
	// private def fieldNamesUncached(clazz:Class[_]):Seq[String]	=
	// 		Reflector constructor clazz get;
			
	// private def classNameFor[T:Manifest]:String	=
	// 		 manifest[T].erasure.getName
			 
	//------------------------------------------------------------------------------
	//## sums of case classes
	
	private object Summand {
		implicit def fromClass[T:Format](clazz:Class[T])():Summand[T]		= Summand(clazz, format[T])
		implicit def fromFormat[T:Manifest](format:Format[T]):Summand[T]	= Summand(manifest[T].erasure, format)
	}
	private case class Summand[T](clazz:Class[_], format:Format[T])
	
	private val typeTag	= JSString("_")
	
	def caseClassSumFormat[T](summands:Summand[_<:T]*):Format[T]	= new Format[T] {
		def write(out:T):JSValue	= {
			val summand	= summands find { _.clazz isInstance out } get;
			// TODO ugly hack
			val sum2:Summand[T]	= summand.asInstanceOf[Summand[T]]
			val typeless		= sum2.format write out
			JSObject(
				typeless.asInstanceOf[JSObject].value	+
				(typeTag	-> JSString(sum2.clazz.getName))
			)
		}
		def read(in:JSValue):T	= {
			val	map			= in.asInstanceOf[JSObject].value
			val className	= map(typeTag).asInstanceOf[JSString].value
			// TODO slow
			val summand		= summands find { _.clazz.getName == className } get;
			summand.format read in
		}
	}
}
