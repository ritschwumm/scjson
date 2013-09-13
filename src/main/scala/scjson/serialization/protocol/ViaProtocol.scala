package scjson.serialization

import scutil.lang._

import scjson._

object ViaProtocol extends ViaProtocol

trait ViaProtocol {
	def via[S,T:Format](adapter:Bijection[S,T]):Format[S]	= 
			adapter andThen format[T]
			
	def viaFunctions[S,T:Format](writeFunc:S=>T, readFunc:T=>S):Format[S]	= 
			via(Bijection(writeFunc, readFunc))
		
	def newType[S,T:Format](marshaller:Marshaller[S,T]):Format[S]	=
			via(marshaller toBijection (it => sys error "cannot read " + it))

	// NOTE here apply is read (and total) and unapply is write (and de-facto total, too)
	def newTypeFunctions[S,T:Format](applyFunc:T=>S, unapplyFunc:S=>Option[T]):Format[S]	=
			via(Bijection(s => unapplyFunc(s) getOrElse (sys error "cannot read " + s), applyFunc))
}
