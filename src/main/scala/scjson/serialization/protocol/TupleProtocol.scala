package scjson.serialization

import scjson._

import JSONSerializationUtil._

object TupleProtocol extends TupleProtocol

trait TupleProtocol extends TupleProtocolGenerated {
	/*
	implicit def Tuple2JSONFormat[T1:JSONFormat,T2:JSONFormat]:JSONFormat[(T1,T2)]	= new JSONFormat[(T1,T2)] {
		def write(out:(T1,T2)):JSONValue	= {
			JSONArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2)
			))
		}
		def read(in:JSONValue):(T1,T2)	= {
			val	arr	= arrayValue(in)
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1))
			)
		}
	}
	*/
}
