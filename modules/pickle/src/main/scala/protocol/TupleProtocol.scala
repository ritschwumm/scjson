package scjson.pickle.protocol

object TupleProtocol extends TupleProtocol

trait TupleProtocol extends TupleProtocolGenerated {
	/*
	implicit def Tuple2Format[T1:Format,T2:Format]:Format[(T1,T2)]	=
			 Format[(T1,T2)](
			 	(out:(T1,T2))	=> {
					JsonArray.Var(
						doWrite[T1](out._1),
						doWrite[T2](out._2)
					)
				},
				(in:JsonValue)	=> {
					val	arr	= arrayValue(in)
					(
						doRead[T1](arr(0)),
						doRead[T2](arr(1))
					)
				}
			)
	*/
}