package scjson.pickle.protocol

import scutil.base.implicits._
import scutil.lang._

import scjson.ast._
import scjson.pickle._

object ObjectSumProtocol extends ObjectSumProtocol

trait ObjectSumProtocol extends SumProtocol {
	def objectSumFormat[T](summands:Summand[T,_<:T]*):Format[T]	=
			sumFormat(summands.toVector map (new ObjectPartialFormat(_).pf))
	
	/** uses an object with a single field where the identifier is the key */
	private class ObjectPartialFormat[T,C<:T](summand:Summand[T,C]) {
		import summand._
		def write(value:T):Option[JSONValue]	=
				castValue(value) map { it =>
					JSONObject.Var(identifier	-> (format write it))
				}
		def read(json:JSONValue):Option[T]	=
				json matchOption {
					case JSONObject.Var((`identifier`, data))	=> format read data
				}
				
		def pf:PartialFormat[T]	= PBijection(write, read)
	}
}
