package scjson.serialization

import scala.reflect._

import scutil.lang._
import scutil.tried._

import scjson._

import JSONSerializationUtil._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	implicit def SeqFormat[T:Format]:Format[Seq[T]] = new Format[Seq[T]] {
		def write(out:Seq[T]):JSONValue	= JSONArray(out map doWrite[T])
		def read(in:JSONValue):Seq[T]	= arrayValue(in) map doRead[T]
	} 
	
	implicit def SetFormat[T:Format]:Format[Set[T]]					= SeqFormat[T] compose Bijection(_.toSeq,	_.toSet)
	implicit def ListFormat[T:Format]:Format[List[T]]				= SeqFormat[T] compose Bijection(_.toSeq,	_.toList)
	implicit def ArrayFormat[T:Format:ClassTag]:Format[Array[T]]	= SeqFormat[T] compose Bijection(_.toSeq,	_.toArray)
	
	//------------------------------------------------------------------------------
		
	/*
	def mapFormat[S,T:Format](conv:Bijection[S,String]):Format[Map[S,T]]	=
			StringMapFormat[T] compose Bijection[Map[S,T],Map[String,T]](
				_ map { case (k,v) => (conv write k, v) },
				_ map { case (k,v) => (conv read  k, v) }
			)
			
	implicit def StringMapFormat[T:Format]:Format[Map[String,T]]	= new Format[Map[String,T]] {
		def write(out:Map[String,T]):JSONValue	=
				JSONObject(out map { 
					case (k,v) => (k, doWrite[T](v)) 
				})
		def read(in:JSONValue):Map[String,T]	= 
				objectValue(in) map { 
					case (k,v) => (k, doRead[T](v)) 
				}
	}
	*/
	
	implicit def ViaSetMapFormat[K:Format,V:Format]:Format[Map[K,V]]	= new Format[Map[K,V]] {
		// TODO dubious
		import TupleProtocol.Tuple2Format
		def write(out:Map[K,V]):JSONValue	= doWrite[Set[(K,V)]](out.toSet)
		def read(in:JSONValue):Map[K,V]		= doRead[Set[(K,V)]](in).toMap
	}
	
	//------------------------------------------------------------------------------
		
	/*
	// alternative 0/1-sized array
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= new Format[Option[T]] {
		def write(out:Option[T]):JSONValue	= doWrite[Seq[T]](out.toSeq)
		def read(in:JSONValue):Option[T]	= doRead[Seq[T]](in) match {
			case Seq(t)	=> Some(t)
			case Seq()	=> None
			case _		=> fail("expected 0 or 1 elements for an Option")
		}
	}
	*/
	
	// alternative {some} or {none}
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= new Format[Option[T]] {
		private val someTag	= "some"
		private val noneTag	= "none"
		
		def write(out:Option[T]):JSONValue	= out match {
			case Some(value)	=> JSONObject(Seq(someTag -> doWrite(value)))
			case None			=> JSONObject(Seq(noneTag -> JSONTrue))
		}
		def read(in:JSONValue):Option[T]	= {
			val map	= objectMap(in)
			(map get someTag, map get noneTag) match {
				case (Some(js), None)	=> Some(doRead[T](js))
				case (None, Some(js))	=> None
				case _					=> fail("unexpected option")
			}
		}
	}
	
	// alternative {left} or {right}
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	= new Format[Either[L,R]] {
		private val rightTag	= "right"
		private val leftTag		= "left"
		
		def write(out:Either[L,R]):JSONValue	= out match {
			case Right(value)	=> JSONObject(Seq(
				rightTag	-> doWrite[R](value)
			))
			case Left(value)	=> JSONObject(Seq(
				leftTag		-> doWrite[L](value)
			))
		}
		def read(in:JSONValue):Either[L,R]	= { 
			val	map	= objectMap(in)
			(map get leftTag, map get rightTag) match {
				case (None, Some(js))	=> Right(doRead[R](js))
				case (Some(js), None)	=> Left(doRead[L](js))
				case _					=> fail("unexpected either")
			}
		}
	}
	
	implicit def TriedFormat[F:Format,W:Format]:Format[Tried[F,W]]	= new Format[Tried[F,W]] {
		private val winTag	= "win"
		private val failTag	= "fail"
		
		def write(out:Tried[F,W]):JSONValue	= out match {
			case Fail(value)	=> JSONObject(Seq(
				failTag	-> doWrite[F](value)
			))
			case Win(value)	=> JSONObject(Seq(
				winTag	-> doWrite[W](value)
			))
		}
		def read(in:JSONValue):Tried[F,W]	= { 
			val	map	= objectMap(in)
			(map get failTag, map get winTag) match {
				case (Some(bs), None)	=> Fail(doRead[F](bs))
				case (None, Some(bs))	=> Win(doRead[W](bs))
				case _					=> fail("unexpected trial")
			}
		}
	}
}
