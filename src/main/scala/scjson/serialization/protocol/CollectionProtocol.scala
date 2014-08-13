package scjson.serialization

import scala.reflect._

import scutil.lang._
import scutil.implicits._

import scjson._

import JSONSerializationUtil._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	/*
	implicit def ISeqFormat[T:Format]:Format[ISeq[T]]	= {
		val sub	= format[T]
		SubtypeFormat[ISeq[T],JSONArray](
			it	=> JSONArray(it map doWrite[T]),
			it	=> it.value map sub.read
		)
	}
	*/
	implicit def ISeqFormat[T:Format]:Format[ISeq[T]] = 
			Format[ISeq[T]](
				(out:ISeq[T])	=> JSONArray(out map doWrite[T]),
				(in:JSONValue)	=> arrayValue(in) map doRead[T]
			)
	
	implicit def SetFormat[T:Format]:Format[Set[T]]					= ISeqFormat[T] compose Bijection(_.toVector,	_.toSet)
	implicit def ListFormat[T:Format]:Format[List[T]]				= ISeqFormat[T] compose Bijection(_.toVector,	_.toList)
	implicit def ArrayFormat[T:Format:ClassTag]:Format[Array[T]]	= ISeqFormat[T] compose Bijection(_.toVector,	_.toArray)
	
	//------------------------------------------------------------------------------
	
	/*
	def mapFormat[S,T:Format](conv:Bijection[S,String]):Format[Map[S,T]]	=
			StringMapFormat[T] compose Bijection[Map[S,T],Map[String,T]](
				_ map { case (k,v) => (conv write k, v) },
				_ map { case (k,v) => (conv read  k, v) }
			)
	
	implicit def StringMapFormat[T:Format]:Format[Map[String,T]]	=
			Format[Map[String,T]](
				(out:Map[String,T])	=> {
					JSONObject(out.toSeq map { 
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
	
	// TODO careful, should sort it's keys maybe
	implicit def MapViaSetFormat[K:Format,V:Format]:Format[Map[K,V]]	= {
		// TODO dubious
		import TupleProtocol.Tuple2Format
		Format[Map[K,V]](
			(out:Map[K,V])	=> doWrite[Set[(K,V)]](out.toSet),
			(in:JSONValue)	=> doRead[Set[(K,V)]](in).toMap
		)
	}
	
	//------------------------------------------------------------------------------
		
	/*
	// alternative 0/1-sized array
	implicit def OptionFormat1[T:Format]:Format[Option[T]]	=
			Format[Option[T]](
				(out:Option[T])	=> doWrite[ISeq[T]](out.toSeq),
				(in:JSONValue)	=> doRead[ISeq[T]](in) match {
					case ISeq(t)	=> Some(t)
					case ISeq()		=> None
					case _			=> fail("expected 0 or 1 elements for an Option")
				}
			)
	*/
	
	// alternative {some} or {none}
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= {
		val someTag	= "some"
		val noneTag	= "none"
		Format[Option[T]](
			_ match {
				case Some(value)	=> JSONVarObject(someTag -> doWrite(value))
				case None			=> JSONVarObject(noneTag -> JSONTrue)
			},
			(in:JSONValue)	=> {
				val map	= objectMap(in)
				(map get someTag, map get noneTag) match {
					case (Some(js), None)	=> Some(doRead[T](js))
					case (None, Some(js))	=> None
					case _					=> fail("unexpected option")
				}
			}
		)
	}
	
	// alternative {left} or {right}
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	= {
		val rightTag	= "right"
		val leftTag		= "left"
		Format[Either[L,R]]( 
			_ match {
				case Right(value)	=> JSONVarObject(
					rightTag	-> doWrite[R](value)
				)
				case Left(value)	=> JSONVarObject(
					leftTag		-> doWrite[L](value)
				)
			},
			(in:JSONValue)	=> {
				val	map	= objectMap(in)
				(map get leftTag, map get rightTag) match {
					case (None, Some(js))	=> Right(doRead[R](js))
					case (Some(js), None)	=> Left(doRead[L](js))
					case _					=> fail("unexpected either")
				}
			}
		)
	}
	
	implicit def TriedFormat[F:Format,W:Format]:Format[Tried[F,W]]	= {
		val winTag	= "win"
		val failTag	= "fail"
		Format[Tried[F,W]]( 
			_ match {
				case Fail(value)	=> JSONVarObject(
					failTag	-> doWrite[F](value)
				)
				case Win(value)		=> JSONVarObject(
					winTag	-> doWrite[W](value)
				)
			},
			(in:JSONValue)	=> { 
				val	map	= objectMap(in)
				(map get failTag, map get winTag) match {
					case (Some(bs), None)	=> Fail(doRead[F](bs))
					case (None, Some(bs))	=> Win(doRead[W](bs))
					case _					=> fail("unexpected trial")
				}
			}
		)
	}
}
