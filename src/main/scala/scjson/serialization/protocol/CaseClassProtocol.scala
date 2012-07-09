package scjson.serialization

import scutil.Implicits._

import scmirror._

import scjson._

import JSONSerializationUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated {
	def caseObjectJSONFormat[T:Manifest](singleton:T):JSONFormat[T]	= {
		new JSONFormat[T] {
			def write(out:T):JSONValue	= {
				JSONObject.empty
			}
			def read(in:JSONValue):T	= {
				singleton
			}
		}
	}
	
	def caseClassJSONFormat1[S1:JSONFormat,T:Manifest](apply:S1=>T, unapply:T=>Option[S1]):JSONFormat[T]	= {
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
	def caseClassJSONFormat2[S1:JSONFormat,S2:JSONFormat,T:Manifest](apply:(S1,S2)=>T, unapply:T=>Option[(S1,S2)]):JSONFormat[T]	= {
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
	protected def fieldNamesFor[T:Manifest]:Seq[String]	=
			(Reflector constructor manifest[T].erasure) getOrError ("cannot get fields for type " + manifest[T].erasure)
		
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
