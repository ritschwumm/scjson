package scjson.serialization

import reflect.runtime.universe._

import scutil.lang._
import scutil.implicits._

import scjson._

import JSONSerializationUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated with SumProtocol {
	def caseObjectFormat[T:TypeTag](singleton:T):Format[T]	=
			Format[T](constant(JSONObject.empty), constant(singleton))
	
	def caseClassFormat1[S1:Format,T:TypeTag](apply:S1=>T, unapply:T=>Option[S1]):Format[T]	= {
		val Seq(k1)	= fieldNamesFor[T]
		Format[T](
			(out:T)	=> {
				val fields	= unapply(out).get
				JSONVarObject(
					k1	-> doWrite[S1](fields)
				)
			},
			(in:JSONValue)	=> {
				val map	= objectMap(in)
				apply(
					doRead[S1](map(k1))
				)
			}
		)
	}
	
	/*
	def caseClassFormat2[S1:Format,S2:Format,T:TypeTag](apply:(S1,S2)=>T, unapply:T=>Option[(S1,S2)]):Format[T]	= {
		val Seq(k1,k2)	= fieldNamesFor[T]
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
			sumFormat(summands map (new InlinePartialFormat(_).pf))
		
	/** injects the type tag as a field with an empty name into an existing object */
	private class InlinePartialFormat[T,C<:T](summand:Summand[T,C]) {
		import summand._
		val typeTag	= ""
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
	
	// BETTER cache results
	protected def fieldNamesFor[T:TypeTag]:Seq[String]	= {
		val typ	= typeOf[T]
		val names:Option[Seq[String]]	=
				for {
					primaryCtor	<- typ.declarations filter { _.isMethod } map { _.asMethod } filter { _.isPrimaryConstructor } singleOption;
					paramNames	<- primaryCtor.paramss.singleOption
				}
				yield paramNames map { _.name.decoded }
		names getOrError ("cannot get fields for type " + typ)
	}
}
