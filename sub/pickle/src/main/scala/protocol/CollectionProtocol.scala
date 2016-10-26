package scjson.pickle.protocol

import scala.reflect.ClassTag

import scutil.lang._

import scjson.ast._
import scjson.pickle._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	implicit def SetFormat[T](implicit ev:Format[ISeq[T]]):Format[Set[T]]				= ev compose Bijection(_.toVector,	_.toSet)
	implicit def ListFormat[T](implicit ev:Format[ISeq[T]]):Format[List[T]]				= ev compose Bijection(_.toVector,	_.toList)
	implicit def ArrayFormat[T:ClassTag](implicit ev:Format[ISeq[T]]):Format[Array[T]]	= ev compose Bijection(_.toVector,	_.toArray)
	
	// TODO careful, should sort it's keys maybe
	implicit def MapViaSetFormat[K:Format,V:Format](implicit ev:Format[Set[(K,V)]]):Format[Map[K,V]]	= {
		Format[Map[K,V]](
			(out:Map[K,V])	=> ev write out.toSet,
			(in:JSONValue)	=> (ev read in).toMap
		)
	}
	
	/*
	def mapFormat[S,T:Format](conv:Bijection[S,String]):Format[Map[S,T]]	=
			StringMapFormat[T] compose Bijection[Map[S,T],Map[String,T]](
				_ map { case (k,v) => (conv write k, v) },
				_ map { case (k,v) => (conv read  k, v) }
			)
	
	implicit def StringMapFormat[T:Format]:Format[Map[String,T]]	=
			Format[Map[String,T]](
				(out:Map[String,T])	=> {
					JSONObject(out.toVector map {
						case (k,v) => (k, doWrite[T](v))
					})
				},
				(in:JSONValue)	=> {
					objectValue(in)
					.map {
						case (k,v) => (k, doRead[T](v))
					}
					.toMap
				}
			)
	*/
}