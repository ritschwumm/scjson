package scjson.serialization

import scala.reflect._

import scutil.Implicits._

import scjson._

import JSONSerializationUtil._

object SumProtocol extends SumProtocol

trait SumProtocol {
	trait PartialJSONFormat[T] {
		def write(value:T):Option[JSONValue]
		def read(json:JSONValue):Option[T]
	}

	def sumJSONFormat[T](partials:Seq[PartialJSONFormat[T]]):JSONFormat[T]	=
			JSONFormat[T](
				(it:T)			=> partials flatMapFirst { _ write it } getOrElse fail("no matching constructor found"),
				(it:JSONValue)	=> partials flatMapFirst { _ read  it } getOrElse fail("no matching constructor found")
			)
			
	//------------------------------------------------------------------------------
	
	object Summand {
		/** DSL for name -> format construction */
		implicit def namedSummand[T,C<:T:ClassTag](pair:(String, JSONFormat[C])):Summand[T,C]	=
				Summand(pair._1, pair._2)
			
		/** identified by runtime class */
		implicit def classTagSummand[T,C<:T:ClassTag](format:JSONFormat[C]):Summand[T,C]	=
				Summand(classTag[C].runtimeClass.getName, format)
	
		/** identified by runtime class */
		implicit def classSummand[T,C<:T:ClassTag:JSONFormat](clazz:Class[C]):Summand[T,C]	=
				Summand(clazz.getName, implicitly[JSONFormat[C]])
	}
	
	/** NOTE this is not erasure-safe */
	case class Summand[T,C<:T:ClassTag](identifier:String, format:JSONFormat[C]) {
		private val tag		= {
			val origTag	= classTag[C]
			val	rtClass	= origTag.runtimeClass
			if (rtClass.isPrimitive)	ClassTag(rtClass.boxed)
			else						origTag
		}
		
		def castValue(value:T):Option[C]	= 
				tag unapply value
	}
}
