package scjson.serialization

import scutil.Implicits._
import scutil.Bijection

import scjson._

import JSONSerializationUtil._

object SumProtocol extends SumProtocol

trait SumProtocol {
	def sumJSONFormat[T](summands:Summand[_<:T]*):JSONFormat[T]	= {
		val helper	= new SumHelper[T](summands)
		JSONFormat[T](
			out	=> {
				val (identifier,formatted)	= helper write out
				JSONObject(Map(identifier	-> formatted))
			},
			in	=> {
				val (identifier,formatted)	= downcast[JSONObject](in).value.head
				helper read (identifier, formatted)
			}
		)
	}
	
	def enumJSONFormat[T](values:Seq[(String,T)]):JSONFormat[T]	=
			JSONFormat[T](
					values map { case (k,v) => (v, JSONString(k)) } toMap,
					values map { case (k,v) => (JSONString(k), v) } toMap)
}
