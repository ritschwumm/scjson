package scjson

import scutil.Bijection

package object serialization {
	type Format[T] = Bijection[T,JSValue]
	def Format[T](writeFunc:T=>JSValue, readFunc:JSValue=>T):Format[T]	= Bijection(writeFunc, readFunc)
}
