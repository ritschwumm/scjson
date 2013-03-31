package scjson.serialization

import scutil.Implicits._

import scjson._

object ObjectSumProtocol extends ObjectSumProtocol

trait ObjectSumProtocol extends SumProtocol {
	def objectSumJSONFormat[T](summands:Summand[T,_<:T]*):JSONFormat[T]	=
			sumJSONFormat(summands map (new ObjectPartialJSONFormat(_)))
	
	/** uses an object with a single field where the identifier is the key */
	private class ObjectPartialJSONFormat[T,C<:T](summand:Summand[T,C]) extends PartialJSONFormat[T] {
		import summand._
		def write(value:T):Option[JSONValue]	=
				castValue(value) map { it =>
					JSONVarObject(identifier	-> (format write it))
				}
		def read(json:JSONValue):Option[T]	=
				json matchOption {
					case JSONObject(Seq((`identifier`, data)))	=> format read data
				}
	}
}
