package scjson.serialization

import scutil.Implicits._

import scjson._

import JSONSerializationUtil._

private class SumHelper[T](summands:Seq[Summand[_<:T]]) {
	def write(out:T):(JSONString,JSONValue)	= 
			try {
				// NOTE as we don't specialize to T, out is boxed
				val summand	= summands find { _.clazz.boxed isInstance out } getOrError "summand not found"
				(JSONString(summand.identifier)	-> (summand.asInstanceOf[Summand[T]].format write out))
			}
			catch {
				case e:ClassCastException	=> fail("cannot write generic sum", e)
			}
			
	def read(identifier:JSONString, in:JSONValue):T	=
			try {
				val summand	= summands find { _.identifier ==== identifier.value } getOrError "summand not found"
				summand.format read in
			}
			catch {
				case e:ClassCastException	=> fail("cannot read generic sum", e)
			}
}