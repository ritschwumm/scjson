package scjson.serialization

import scutil.Bijection

import scjson._

import Operations._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	implicit def SeqFormat[T:Format]:Format[Seq[T]] = new Format[Seq[T]] {
		def write(out:Seq[T]):JSValue	= JSArray(out map doWrite[T])
		def read(in:JSValue):Seq[T]		= in.asInstanceOf[JSArray].value map doRead[T]
	} 
	
	implicit def SetFormat[T:Format]:Format[Set[T]]					= viaSeq[Set[T],T](_.toSet)
	implicit def ListFormat[T:Format]:Format[List[T]]				= viaSeq[List[T],T](_.toList)
	// implicit def ArrayFormat[T:Format:Manifest]:Format[Array[T]]	= viaSeq[Array[T],T](_.toArray)
	// implicit def SetFormat[T:Format]:Format[Set[T]]				= SeqFormat[T] andThen Bijection(_.toSeq,	_.toSet)
	// implicit def ListFormat[T:Format]:Format[List[T]]			= SeqFormat[T] andThen Bijection(_.toSeq,	_.toList)
	implicit def ArrayFormat[T:Format:Manifest]:Format[Array[T]]	= SeqFormat[T] compose Bijection(_.toSeq,	_.toArray)
	
	def viaSeq[M<:Traversable[T],T:Format](readFunc:Seq[T]=>M):Format[M]	=
			SeqFormat[T] compose Bijection(_.toSeq, readFunc)
			
	//------------------------------------------------------------------------------
		
	/** map format with arbitrary keys */
	def mapFormat[S,T:Format](conv:Bijection[S,String]):Format[Map[S,T]]	=
			StringMapFormat[T] compose Bijection[Map[S,T],Map[String,T]](
				_ map { case (k,v) => (conv write k, v) },
				_ map { case (k,v) => (conv read  k, v) }
			)
			
	implicit def StringMapFormat[T:Format]:Format[Map[String,T]]	= new Format[Map[String,T]] {
		def write(out:Map[String,T]):JSValue	=
				JSObject(out map { 
					case (k,v) => (JSString(k), doWrite[T](v)) 
				})
		def read(in:JSValue):Map[String,T]	= 
				in.asInstanceOf[JSObject].value map { 
					case (k,v) => (k.value, doRead[T](v)) 
				}
	}
	
	implicit def ViaSetMapFormat[K:Format,V:Format]:Format[Map[K,V]]	= new Format[Map[K,V]] {
		// TODO dubious
		import TupleProtocol.Tuple2Format
		def write(out:Map[K,V]):JSValue	= doWrite[Set[(K,V)]](out.toSet)
		def read(in:JSValue):Map[K,V]	= doRead[Set[(K,V)]](in).toMap
	}
	
	//------------------------------------------------------------------------------
			
	// def enumFormat[T](conv:Bijection[String,T]):Format[T]	= StringFormat andThen conv
		
	
	// NOTE could be a key/non-key object, too
	
	/*
	// alternative null/not-null
	// NOTE this doesn't work with Option[Option[T]]
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= new Format[Option[T]] {
		def write(out:Option[T]):JSValue	= out match {
			case Some(v)	=> doWrite[T](v)
			case None		=> JSNull
		}
		def read(in:JSValue):Option[T]		= in match {
			case JSNull	=> None
			case _		=> Some(doRead[T](in))
		}
	}
	*/
	
	// alternative 0/1-sized array
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= new Format[Option[T]] {
		def write(out:Option[T]):JSValue	= doWrite[Seq[T]](out.toSeq)
		def read(in:JSValue):Option[T]		= doRead[Seq[T]](in) match {
			case Seq(t)	=> Some(t)
			case Seq()	=> None
			case _		=> sys error "expected 0 or 1 elements for an Option"
		}
	}
	
	// alternative boolean + value
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	= new Format[Either[L,R]] {
		private val typeTag		= JSString("_")
		private val contentTag	= JSString("*")
		def write(out:Either[L,R]):JSValue	= out match {
			case Right(value)	=> JSObject(Map(
				typeTag		-> JSTrue,
				contentTag	-> doWrite[R](value)
			))
			case Left(value)	=> JSObject(Map(
				typeTag		-> JSFalse,
				contentTag	-> doWrite[L](value)
			))
		}
		def read(in:JSValue):Either[L,R]	= { 
			val	map	= in.asInstanceOf[JSObject].value
			val js	= map(contentTag)
			map(typeTag) match {
				case JSTrue		=> Right(doRead[R](js))
				case JSFalse	=> Left(doRead[L](js))
				case _			=> sys error "unexpected right flag"
			}
		}
	}
	
	/*
	// alternative {left} or {right}
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	= new Format[Either[L,R]] {
		private val rightTag	= JSString(">")
		private val leftTag		= JSString("<")
		def write(out:Either[L,R]):JSValue	= out match {
			case Right(value)	=> JSObject(Map(
				rightTag	-> doWrite[R](value)
			))
			case Left(value)	=> JSObject(Map(
				leftTag		-> doWrite[L](value)
			))
		}
		def read(in:JSValue):Either[L,R]	= { 
			val	map	= in.asInstanceOf[JSObject].value
			(map get leftTag, map get rightTag) match {
				case (None, Some(js))	=> Right(doRead[R](js))
				case (Some(js), None)	=> Left(doRead[L](js))
				case _					=> sys error "unexpected either"
			}
		}
	}
	*/
}
