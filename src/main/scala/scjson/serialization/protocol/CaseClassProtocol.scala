package scjson.serialization

import reflect.runtime.universe._

import scutil.Implicits._

import scjson._

import JSONSerializationUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated {
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
		
	//------------------------------------------------------------------------------
	//## sums of case classes
	
	def caseClassSumJSONFormat[T](summands:Summand[_<:T]*):JSONFormat[T]	= {
		val helper	= new SumHelper[T](summands)
		JSONFormat[T](
			out => {
				val (identifier,formatted)	= helper write out
				JSONObject(
					(Summand.typeTag -> identifier)	+:
					downcast[JSONObject](formatted).value
				)
			},
			in => {
				val formatted	= downcast[JSONObject](in)
				val identifier	= downcast[JSONString](formatted valueMap Summand.typeTag)
				helper read (identifier, formatted)
			}
		)
	}
}
