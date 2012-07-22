package scjson.serialization

import scutil.lang._
import scutil.tried._

import scjson._

import JSONSerializationUtil._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	implicit def SeqJSONFormat[T:JSONFormat]:JSONFormat[Seq[T]] = new JSONFormat[Seq[T]] {
		def write(out:Seq[T]):JSONValue	= JSONArray(out map doWrite[T])
		def read(in:JSONValue):Seq[T]	= arrayValue(in) map doRead[T]
	} 
	
	implicit def SetJSONFormat[T:JSONFormat]:JSONFormat[Set[T]]					= SeqJSONFormat[T] compose Bijection(_.toSeq,	_.toSet)
	implicit def ListJSONFormat[T:JSONFormat]:JSONFormat[List[T]]				= SeqJSONFormat[T] compose Bijection(_.toSeq,	_.toList)
	implicit def ArrayJSONFormat[T:JSONFormat:Manifest]:JSONFormat[Array[T]]	= SeqJSONFormat[T] compose Bijection(_.toSeq,	_.toArray)
	
	//------------------------------------------------------------------------------
		
	/*
	def mapJSONFormat[S,T:JSONFormat](conv:Bijection[S,String]):JSONFormat[Map[S,T]]	=
			StringMapJSONFormat[T] compose Bijection[Map[S,T],Map[String,T]](
				_ map { case (k,v) => (conv write k, v) },
				_ map { case (k,v) => (conv read  k, v) }
			)
			
	implicit def StringMapJSONFormat[T:JSONFormat]:JSONFormat[Map[String,T]]	= new JSONFormat[Map[String,T]] {
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
	
	implicit def ViaSetMapJSONFormat[K:JSONFormat,V:JSONFormat]:JSONFormat[Map[K,V]]	= new JSONFormat[Map[K,V]] {
		// TODO dubious
		import TupleProtocol.Tuple2JSONFormat
		def write(out:Map[K,V]):JSONValue	= doWrite[Set[(K,V)]](out.toSet)
		def read(in:JSONValue):Map[K,V]		= doRead[Set[(K,V)]](in).toMap
	}
	
	//------------------------------------------------------------------------------
		
	/*
	// alternative 0/1-sized array
	implicit def OptionJSONFormat[T:JSONFormat]:JSONFormat[Option[T]]	= new JSONFormat[Option[T]] {
		def write(out:Option[T]):JSONValue	= doWrite[Seq[T]](out.toSeq)
		def read(in:JSONValue):Option[T]	= doRead[Seq[T]](in) match {
			case Seq(t)	=> Some(t)
			case Seq()	=> None
			case _		=> fail("expected 0 or 1 elements for an Option")
		}
	}
	*/
	
	// alternative {some} or {none}
	implicit def OptionJSONFormat[T:JSONFormat]:JSONFormat[Option[T]]	= new JSONFormat[Option[T]] {
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
	implicit def EitherJSONFormat[L:JSONFormat,R:JSONFormat]:JSONFormat[Either[L,R]]	= new JSONFormat[Either[L,R]] {
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
	
	implicit def TriedJSONFormat[F:JSONFormat,W:JSONFormat]:JSONFormat[Tried[F,W]]	= new JSONFormat[Tried[F,W]] {
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
