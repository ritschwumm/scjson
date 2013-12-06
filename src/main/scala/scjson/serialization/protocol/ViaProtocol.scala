package scjson.serialization

import scutil.lang._
import scutil.implicits._

import scjson._

object ViaProtocol extends ViaProtocol

trait ViaProtocol {
	def via[S,T:Format](adapter:Bijection[S,T]):Format[S]	= 
			adapter andThen format[T]
			
	def viaFunctions[S,T:Format](writeFunc:S=>T, readFunc:T=>S):Format[S]	= 
			via(Bijection(writeFunc, readFunc))
}
