package scjson.serialization

import scutil.lang._

import scjson._

object ViaProtocol extends ViaProtocol

trait ViaProtocol {
	def via[S,T:JSONFormat](adapter:Bijection[S,T]):JSONFormat[S]	= 
			adapter andThen jsonFormat[T]
			
	def viaFunctions[S,T:JSONFormat](writeFunc:S=>T, readFunc:T=>S):JSONFormat[S]	= 
			via(Bijection(writeFunc, readFunc))
		
	def newType[S,T:JSONFormat](marshaller:Marshaller[S,T]):JSONFormat[S]	=
			via(marshaller.asBijectionFailing)

	def newTypeFunctions[S,T:JSONFormat](applyFunc:T=>S, unapplyFunc:S=>Option[T]):JSONFormat[S]	=
			via(Bijection marshallerFailing (applyFunc, unapplyFunc) inverse)
}
