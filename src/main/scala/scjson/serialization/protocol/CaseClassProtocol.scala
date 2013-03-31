package scjson.serialization

import reflect.runtime.universe._

import scutil.Implicits._

import scjson._

import JSONSerializationUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated with SumProtocol {
	def caseObjectJSONFormat[T:TypeTag](singleton:T):JSONFormat[T]	= {
		new JSONFormat[T] {
			def write(out:T):JSONValue	= {
				JSONObject.empty
			}
			def read(in:JSONValue):T	= {
				singleton
			}
		}
	}
	
	def caseClassJSONFormat1[S1:JSONFormat,T:TypeTag](apply:S1=>T, unapply:T=>Option[S1]):JSONFormat[T]	= {
		val Seq(k1)	= fieldNamesFor[T]
		new JSONFormat[T] {
			def write(out:T):JSONValue	= {
				val fields	= unapply(out).get
				JSONObject(Seq(
					k1	-> doWrite[S1](fields)
				))
			}
			def read(in:JSONValue):T	= {
				val map	= objectMap(in)
				apply(
					doRead[S1](map(k1))
				)
			}
		}
	}
	
	/*
	def caseClassJSONFormat2[S1:JSONFormat,S2:JSONFormat,T:TypeTag](apply:(S1,S2)=>T, unapply:T=>Option[(S1,S2)]):JSONFormat[T]	= {
		val Seq(k1,k2)	= fieldNamesFor[T]
		new JSONFormat[T] {
			def write(out:T):JSONValue	= {
				val fields	= unapply(out).get
				JSONObject(Map(
					k1	-> doWrite[S1](fields._1),
					k2	-> doWrite[S2](fields._2)
				))
			}
			def read(in:JSONValue):T	= {
				val map	= objectValue(in)
				apply(
					doRead[S1](map(k1)),
					doRead[S2](map(k2))
				)
			}
		}
	}
	*/
	
	/** uses a field with an empty name for the specific constructor */
	def caseClassSumJSONFormat[T](summands:Summand[T,_<:T]*):JSONFormat[T]	=
			sumJSONFormat(summands map (new InlinePartialJSONFormat(_)))
		
	/** injects the type tag as a field with an empty name into an existing object */
	private class InlinePartialJSONFormat[T,C<:T](summand:Summand[T,C]) extends PartialJSONFormat[T] {
		import summand._
		val typeTag	= ""
		def write(value:T):Option[JSONValue]	=
				castValue(value) map { it =>
					JSONVarObject(typeTag -> JSONString(identifier)) ++ 
					downcast[JSONObject](format write it)
				}
		def read(json:JSONValue):Option[T]	=
				downcast[JSONObject](json).value 
				.exists	{ _ == (typeTag, JSONString(identifier)) } 
				.guard	{ format read json }
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
