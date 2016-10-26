package scjson.pickle.protocol

import reflect.runtime.universe._

import scutil.base.implicits._
import scutil.lang._

import scjson.ast._
import scjson.pickle._

import JSONPickleUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated with SumProtocol {
	def caseObjectFormat[T:TypeTag](singleton:T):Format[T]	=
			Format[T](constant(JSONObject.empty), constant(singleton))
	
	def caseClassFormat1[S1:Format,T:Fielding](apply:S1=>T, unapply:T=>Option[S1]):Format[T]	= {
		val ISeq(k1)	= Fielder[T]
		Format[T](
			(out:T)	=> {
				val fields	= unapplyTotal(unapply, out)
				JSONVarObject(
					k1	-> doWrite[S1](fields)
				)
			},
			(in:JSONValue)	=> {
				val map	= objectMap(in)
				apply(
					doReadUnsafe[S1](map(k1))
				)
			}
		)
	}
	
	/*
	def caseClassFormat2[S1:Format,S2:Format,T:Fielding](apply:(S1,S2)=>T, unapply:T=>Option[(S1,S2)]):Format[T]	= {
		val ISeq(k1,k2)	= Fielder[T]
		Format[T](
			(out:T)	=> {
				val fields	= unapply(out).get
				JSONVarDocument(
					k1	-> doWrite[S1](fields._1),
					k2	-> doWrite[S2](fields._2)
				)
			},
			(in:JSONValue)	=> {
				val map	= objectMap(in)
				apply(
					doRead[S1](map(k1)),
					doRead[S2](map(k2))
				)
			}
		)
	}
	*/
	
	/** uses a field with an empty name for the specific constructor */
	def caseClassSumFormat[T](summands:Summand[T,_<:T]*):Format[T]	=
			sumFormat(summands.toVector map (new InlinePartialFormat(_).pf))
		
	private val typeTag	= ""
	
	/** injects the type tag as a field with an empty name into an existing object */
	private class InlinePartialFormat[T,C<:T](summand:Summand[T,C]) {
		import summand._
		
		def write(value:T):Option[JSONValue]	=
				castValue(value) map { it =>
					JSONVarObject(typeTag -> JSONString(identifier)) ++
					downcast[JSONObject](format write it)
				}
		def read(json:JSONValue):Option[T]	=
				objectValue(json)
				.exists	{ _ == (typeTag, JSONString(identifier)) }
				.guard	{ format read json }
				
		def pf:PartialFormat[T]	= PBijection(write, read)
	}
}
