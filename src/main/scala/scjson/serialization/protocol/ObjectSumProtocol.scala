package scjson.serialization

import scutil.lang._
import scutil.implicits._

import scjson._

object ObjectSumProtocol extends ObjectSumProtocol

trait ObjectSumProtocol extends SumProtocol {
	def objectSumFormat[T](summands:Summand[T,_<:T]*):Format[T]	=
			sumFormat(summands map (new ObjectPartialFormat(_).pf))
	
	/** uses an object with a single field where the identifier is the key */
	private class ObjectPartialFormat[T,C<:T](summand:Summand[T,C]) {
		import summand._
		def write(value:T):Option[JSONValue]	=
				castValue(value) map { it =>
					JSONVarObject(identifier	-> (format write it))
				}
		def read(json:JSONValue):Option[T]	=
				json matchOption {
					case JSONVarObject((`identifier`, data))	=> format read data
				}
				
		def pf:PartialFormat[T]	= PBijection(write, read)
	}
}
