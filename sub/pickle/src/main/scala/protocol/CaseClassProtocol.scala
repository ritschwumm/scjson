package scjson.pickle.protocol

import reflect.runtime.universe._

import scutil.base.implicits._
import scutil.lang._

import scjson.ast._
import scjson.pickle._

import JsonPickleUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated with SumProtocol {
	def caseObjectFormat[T:TypeTag](singleton:T):Format[T]	=
			Format[T](constant(JsonObject.empty), constant(singleton))
	
	def caseClassFormat1[S1:Format,T](apply:S1=>T, unapply:T=>Option[S1])(implicit FN:FieldNames[T]):Format[T]	= {
		val Vector(k1)	= FN.names
		Format[T](
			(out:T)	=> {
				val fields	= unapplyTotal(unapply, out)
				JsonObject.Var(
					k1	-> doWrite[S1](fields)
				)
			},
			(in:JsonValue)	=> {
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
				JsonVarDocument(
					k1	-> doWrite[S1](fields._1),
					k2	-> doWrite[S2](fields._2)
				)
			},
			(in:JsonValue)	=> {
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
		
		def write(value:T):Option[JsonValue]	=
				castValue(value) map { it =>
					JsonObject.Var(typeTag -> JsonString(identifier)) ++
					downcast[JsonObject](format write it)
				}
		def read(json:JsonValue):Option[T]	=
				objectValue(json)
				.exists	{ _ == ((typeTag, JsonString(identifier))) }
				.option	{ format read json }
				
		def pf:PartialFormat[T]	= PBijection(write, read)
	}
}
