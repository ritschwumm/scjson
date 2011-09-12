package scjson.reflection.scalasignature

import java.util.Arrays

import scala.collection.mutable

import scala.reflect.ScalaSignature
import scala.reflect.generic.ByteCodecs
import scala.reflect.generic.PickleFormat
import scala.reflect.generic.PickleBuffer

import scutil.Tuples
import scutil.ext.BooleanImplicits._

object Decoder {
	def decode(clazz:Class[_]):Option[Signature]	= 
			getAnnotation(clazz)	map 
			decodeAnnotation		map 
			parseSignature			map 
			{ it => new Signature(it) }

	// TODO what is ScalaLongSignature?

	def getAnnotation(clazz:Class[_]):Option[ScalaSignature]	= 
			Option(clazz getAnnotation classOf[ScalaSignature])
	
	/*
	// NOTE this fails for 0 bytes in code
	def decodeAnnotation(s:ScalaSignature):Array[Byte] = {
		val code	= s.bytes getBytes "UTF-8"
		val len		= ByteCodecs decode code
		Arrays copyOf (code, len)
	}
	*/
	
	def decodeAnnotation(s:ScalaSignature):Array[Byte] = {
		val raw	= s.bytes.toCharArray map { it => (it - 1) & 0x7f toByte }
		val sze	= raw.size * 7 / 8
		val out	= new Array[Byte](sze)
		// @see ByteCodecs#decode7to8
		var i = 0
		while (i <  sze) {
			val byt = i + (i / 7)
			val bit = i % 7
			out(i)	=
					(raw(byt+0) >> (bit+0)) |
					(raw(byt+1) << (7-bit)) toByte;
			i	+= 1
		}
		out
	}
	
	def parseSignature(bytes:Array[Byte]):Seq[Entry] = {
		val pb		= pickleBuffer(bytes)
		val major	= pb.readNat
		val minor	= pb.readNat
		// println("version=" + major + "." + minor)
		require(major == PickleFormat.MajorVersion && minor <= PickleFormat.MinorVersion,	"wrong version")
		
		pb.toIndexedSeq.zipWithIndex map Tuples.runcurry3 collect {
			case (PickleFormat.TERMname,			body, index)	=> readTERMname(			Ref(index), pickleBuffer(body))
			case (PickleFormat.TYPEname,			body, index)	=> readTYPEname(			Ref(index), pickleBuffer(body))
			
			case (PickleFormat.TYPEsym,				body, index)	=> readTYPEsym(				Ref(index), pickleBuffer(body))
			case (PickleFormat.ALIASsym,			body, index)	=> readALIASsym(			Ref(index), pickleBuffer(body))
			case (PickleFormat.MODULEsym,			body, index)	=> readMODULEsym(			Ref(index), pickleBuffer(body))
			case (PickleFormat.CLASSsym,			body, index)	=> readCLASSsym(			Ref(index), pickleBuffer(body))
			case (PickleFormat.VALsym,				body, index)	=> readVALsym(				Ref(index), pickleBuffer(body))
			case (PickleFormat.NONEsym,				body, index)	=> readNONEsym(				Ref(index), pickleBuffer(body))
			
			case (PickleFormat.EXTref,				body, index)	=> readEXTref(				Ref(index), pickleBuffer(body))
			case (PickleFormat.EXTMODCLASSref,		body, index)	=> readEXTMODCLASSref(		Ref(index), pickleBuffer(body))
			
			case (PickleFormat.THIStpe,				body, index)	=> readTHIStpe(				Ref(index), pickleBuffer(body))
			case (PickleFormat.SINGLEtpe,			body, index)	=> readSINGLEtpe(			Ref(index), pickleBuffer(body))
			case (PickleFormat.CONSTANTtpe,			body, index)	=> readCONSTANTtpe(			Ref(index), pickleBuffer(body))
			case (PickleFormat.TYPEBOUNDStpe,		body, index)	=> readTYPEBOUNDStpe(		Ref(index), pickleBuffer(body))
			case (PickleFormat.SUPERtpe,			body, index)	=> readSUPERtpe(			Ref(index), pickleBuffer(body))
			case (PickleFormat.TYPEREFtpe,			body, index)	=> readTYPEREFtpe(			Ref(index), pickleBuffer(body))
			case (PickleFormat.REFINEDtpe,			body, index)	=> readREFINEDtpe(			Ref(index), pickleBuffer(body))
			case (PickleFormat.CLASSINFOtpe,		body, index)	=> readCLASSINFOtpe(		Ref(index), pickleBuffer(body))
			case (PickleFormat.METHODtpe,			body, index)	=> readMETHODtpe(			Ref(index), pickleBuffer(body))
			case (PickleFormat.POLYtpe,				body, index)	=> readPOLYtpe(				Ref(index), pickleBuffer(body))
			case (PickleFormat.IMPLICITMETHODtpe,	body, index)	=> readIMPLICITMETHODtpe(	Ref(index), pickleBuffer(body))
			case (PickleFormat.ANNOTATEDtpe,		body, index)	=> readANNOTATEDtpe(		Ref(index), pickleBuffer(body))
			case (PickleFormat.EXISTENTIALtpe,		body, index)	=> readEXISTENTIALtpe(		Ref(index), pickleBuffer(body))
			
			case (PickleFormat.LITERALboolean,		body, index)	=> readLITERALboolean(		Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALbyte,			body, index)	=> readLITERALbyte(			Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALshort,		body, index)	=> readLITERALshort(		Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALchar,			body, index)	=> readLITERALchar(			Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALint,			body, index)	=> readLITERALint(			Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALlong,			body, index)	=> readLITERALlong(			Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALfloat,		body, index)	=> readLITERALfloat(		Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALdouble,		body, index)	=> readLITERALdouble(		Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALstring,		body, index)	=> readLITERALstring(		Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALnull,			body, index)	=> readLITERALnull(			Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALclass,		body, index)	=> readLITERALclass(		Ref(index), pickleBuffer(body))
			case (PickleFormat.LITERALenum,			body, index)	=> readLITERALenum(			Ref(index), pickleBuffer(body))
			
			case (PickleFormat.SYMANNOT,			body, index)	=> readSYMANNOT(			Ref(index), pickleBuffer(body))
			case (PickleFormat.CHILDREN,			body, index)	=> readCHILDREN(			Ref(index), pickleBuffer(body))
			case (PickleFormat.ANNOTINFO,			body, index)	=> readANNOTINFO(			Ref(index), pickleBuffer(body))
			case (PickleFormat.ANNOTARGARRAY,		body, index)	=> readANNOTARGARRAY(		Ref(index), pickleBuffer(body))
			
			case (tag,	body, index)								=> UNKNOWN(					Ref(index), tag, body)
		} 
	}
	
	def readTERMname(self:Ref, pb:PickleBuffer):TERMname	= TERMname(self, readNameInfo(pb))
	def readTYPEname(self:Ref, pb:PickleBuffer):TYPEname	= TYPEname(self, readNameInfo(pb))
	
	def readTYPEsym(self:Ref, pb:PickleBuffer):TYPEsym		= TYPEsym(self, readSymbolInfo(pb))
	def readALIASsym(self:Ref, pb:PickleBuffer):ALIASsym	= ALIASsym(self, readSymbolInfo(pb))
	
	// TODO pickler could write an alias at the end, too??
	def readMODULEsym(self:Ref, pb:PickleBuffer):MODULEsym	= MODULEsym(self, readSymbolInfo(pb))
	
	def readCLASSsym(self:Ref, pb:PickleBuffer):CLASSsym	= {
		val symbolInfo	= readSymbolInfo(pb)
		val thistype	= unfinished(pb) guard readRef(pb)
		CLASSsym(self, symbolInfo, thistype)
	}
	
	def readVALsym(self:Ref, pb:PickleBuffer):VALsym	= {
		// NOTE defaultGetter_Ref is no longer written
		val symbolInfo	= readSymbolInfo(pb)
		val alias		= unfinished(pb) guard readRef(pb)
		VALsym(self, symbolInfo, alias)
	}
	
	def readNONEsym(self:Ref, pb:PickleBuffer):NONEsym	= NONEsym(self)
	
	def readEXTref(self:Ref, pb:PickleBuffer):EXTref	= {
		val	name	= readRef(pb)
		val owner	= unfinished(pb) guard readRef(pb)
		EXTref(self, name, owner)
	}
	
	def readEXTMODCLASSref(self:Ref, pb:PickleBuffer):EXTMODCLASSref	= {
		val	name	= readRef(pb)
		val owner	= unfinished(pb) guard readRef(pb)
		EXTMODCLASSref(self, name, owner)
	}
	
	def readTHIStpe(self:Ref, pb:PickleBuffer):THIStpe	= {
		val	sym	= readRef(pb)
		THIStpe(self, sym)
	}
	
	def readSINGLEtpe(self:Ref, pb:PickleBuffer):SINGLEtpe	= {
		val	pre	= readRef(pb)
		val	sym	= readRef(pb)
		SINGLEtpe(self, pre, sym)
	}
	
	def readCONSTANTtpe(self:Ref, pb:PickleBuffer):CONSTANTtpe	= {
		val	constant	= readRef(pb)
		CONSTANTtpe(self, constant)
	}
	
	def readTYPEBOUNDStpe(self:Ref, pb:PickleBuffer):TYPEBOUNDStpe	= {
		val	lo	= readRef(pb)
		val	hi	= readRef(pb)
		TYPEBOUNDStpe(self, lo, hi)
	}
	
	def readSUPERtpe(self:Ref, pb:PickleBuffer):SUPERtpe	= {
		val	thisT	= readRef(pb)
		val	superT	= readRef(pb)
		SUPERtpe(self, thisT, superT)
	}
	
	def readTYPEREFtpe(self:Ref, pb:PickleBuffer):TYPEREFtpe	= {
		val	pre		= readRef(pb)
		val	sym		= readRef(pb)
		val targ	= readRefList(pb)
		TYPEREFtpe(self, pre, sym, targ)
	}
	
	def readREFINEDtpe(self:Ref, pb:PickleBuffer):REFINEDtpe	= {
		val	classsym	= readRef(pb)
		val parent		= readRefList(pb)
		REFINEDtpe(self, classsym, parent)
	}
	
	def readCLASSINFOtpe(self:Ref, pb:PickleBuffer):CLASSINFOtpe	= {
		val	classsym	= readRef(pb)
		val parent		= readRefList(pb)
		CLASSINFOtpe(self, classsym, parent)
	}
	
	def readMETHODtpe(self:Ref, pb:PickleBuffer):METHODtpe	= {
		val	result	= readRef(pb)
		val formals	= readRefList(pb)
		METHODtpe(self, result, formals)
	}
	
	def readPOLYtpe(self:Ref, pb:PickleBuffer):POLYtpe	= {
		val	result	= readRef(pb)
		val targ	= readRefList(pb)
		POLYtpe(self, result, targ)
	}
	
	def readIMPLICITMETHODtpe(self:Ref, pb:PickleBuffer):IMPLICITMETHODtpe	= {
		val	result	= readRef(pb)
		val formals	= readRefList(pb)
		IMPLICITMETHODtpe(self, result, formals)
	}
	
	def readANNOTATEDtpe(self:Ref, pb:PickleBuffer):ANNOTATEDtpe	= {
		val	underlying	= readRef(pb)
		// TODO readRef(pb)
		// if (settings.selfInAnnots.value) putSymbol(selfsym)
		val	selfsym		= None
		// TODO hack, in fact this is much more complicated
		// see Pickler#writeAnnotation
		val annotinfo	= readRefList(pb)
		ANNOTATEDtpe(self, underlying, selfsym, annotinfo)
	}
	
	def readEXISTENTIALtpe(self:Ref, pb:PickleBuffer):EXISTENTIALtpe	= {
		val	result	= readRef(pb)
		val params	= readRefList(pb)
		EXISTENTIALtpe(self, result, params)
	}
	
	def readLITERALboolean(self:Ref, pb:PickleBuffer):LITERALboolean	= LITERALboolean(self, pb readLong left(pb)) 
	def readLITERALbyte(self:Ref, pb:PickleBuffer):LITERALbyte			= LITERALbyte(self, pb readLong left(pb))
	def readLITERALshort(self:Ref, pb:PickleBuffer):LITERALshort		= LITERALshort(self, pb readLong left(pb))
	def readLITERALchar(self:Ref, pb:PickleBuffer):LITERALchar			= LITERALchar(self, pb readLong left(pb))
	def readLITERALint(self:Ref, pb:PickleBuffer):LITERALint			= LITERALint(self, pb readLong left(pb))
	def readLITERALlong(self:Ref, pb:PickleBuffer):LITERALlong			= LITERALlong(self, pb readLong left(pb))
	def readLITERALfloat(self:Ref, pb:PickleBuffer):LITERALfloat		= LITERALfloat(self, pb readLong left(pb))
	def readLITERALdouble(self:Ref, pb:PickleBuffer):LITERALdouble		= LITERALdouble(self, pb readLong left(pb))
	def readLITERALstring(self:Ref, pb:PickleBuffer):LITERALstring		= LITERALstring(self, readRef(pb))
	def readLITERALnull(self:Ref, pb:PickleBuffer):LITERALnull			= LITERALnull(self)
	def readLITERALclass(self:Ref, pb:PickleBuffer):LITERALclass		= LITERALclass(self, readRef(pb))
	def readLITERALenum(self:Ref, pb:PickleBuffer):LITERALenum			= LITERALenum(self, readRef(pb))
			
	def readSYMANNOT(self:Ref, pb:PickleBuffer):SYMANNOT	= {
		val	sym				= readRef(pb)
		val annotInfoBody	= readAnnotInfoBody(pb)
		SYMANNOT(self,sym, annotInfoBody)
	}
	
	def readCHILDREN(self:Ref, pb:PickleBuffer):CHILDREN	= {
		val sym		= readRef(pb)
		val sym1	= readRefList(pb)
		CHILDREN(self, sym, sym1)
	}
	
	def readANNOTINFO(self:Ref, pb:PickleBuffer):ANNOTINFO			= ANNOTINFO(self, readAnnotInfoBody(pb))
	def readANNOTARGARRAY(self:Ref, pb:PickleBuffer):ANNOTARGARRAY	= ANNOTARGARRAY(self, readRefList(pb))
	
	//------------------------------------------------------------------------------
	
	def readNameInfo(pb:PickleBuffer):NameInfo	= NameInfo(new String(remainder(pb), "UTF-8"))
	
	def readSymbolInfo(pb:PickleBuffer):SymbolInfo	= {
		val	name			= readRef(pb)
		val owner			= readRef(pb)
		val flags			= readFlags(pb)
		// TODO optional value.
		// read it, find out if it's referencing a Symbol, if not it's left out
		// this is what the pickler does: 
		// if (sym.hasAccessBoundary) writeRef(sym.privateWithin)
		val privateWithin	= None	// readRef(pb)
		val info			= readRef(pb)
		SymbolInfo(name, owner, flags, privateWithin, info)
	}
	
	def readAnnotInfoBody(pb:PickleBuffer):AnnotInfoBody	= {
		val annotArg	= Seq.empty	// TODO parse this. how?
		val const		= readRefList(pb) grouped 2 collect { case Seq(a,b) => Pair(a,b) } toSeq;
		AnnotInfoBody(annotArg, const)
	}
	
	def readFlags(pb:PickleBuffer):Set[Flag]	= Flag decodePickled pb.readLongNat
	
	def readRefList(pb:PickleBuffer):Seq[Ref]	= {
		val buf	= mutable.ListBuffer.empty[Ref]
		while (unfinished(pb))	buf	+= readRef(pb)
		buf
	}
		
	def readRef(pb:PickleBuffer):Ref	= Ref(pb.readNat)
	
	//------------------------------------------------------------------------------
	
	private def pickleBuffer(bytes:Array[Byte]):PickleBuffer	= new PickleBuffer(bytes, 0, -1)
	private def unfinished(pb:PickleBuffer):Boolean		= left(pb) > 0
	private def left(pb:PickleBuffer):Int				= pb.bytes.length - pb.readIndex
	private def remainder(pb:PickleBuffer):Array[Byte]	= pb.bytes slice (pb.readIndex, left(pb))
	
	//------------------------------------------------------------------------------
	
	/*
	def tagName(tag:Int):String = {
		import PickleFormat._
		tag match {
			case PickleFormat.TERMname			=> "TERMname"
			case PickleFormat.TYPEname			=> "TYPEname"
			case PickleFormat.NONEsym			=> "NONEsym"
			case PickleFormat.TYPEsym			=> "TYPEsym"
			case PickleFormat.ALIASsym			=> "ALIASsym"
			case PickleFormat.CLASSsym			=> "CLASSsym"
			case PickleFormat.MODULEsym			=> "MODULEsym"
			case PickleFormat.VALsym			=> "VALsym"
			case PickleFormat.EXTref			=> "EXTref"
			case PickleFormat.EXTMODCLASSref 	=> "EXTMODCLASSref"
			case PickleFormat.NOtpe				=> "NOtpe"
			case PickleFormat.NOPREFIXtpe		=> "NOPREFIXtpe"
			case PickleFormat.THIStpe			=> "THIStpe"
			case PickleFormat.SINGLEtpe			=> "SINGLEtpe"
			case PickleFormat.CONSTANTtpe		=> "CONSTANTtpe"
			case PickleFormat.TYPEREFtpe		=> "TYPEREFtpe"
			case PickleFormat.TYPEBOUNDStpe		=> "TYPEBOUNDStpe"
			case PickleFormat.REFINEDtpe		=> "REFINEDtpe"
			case PickleFormat.CLASSINFOtpe		=> "CLASSINFOtpe"
			case PickleFormat.METHODtpe			=> "METHODtpe"
			case PickleFormat.POLYtpe			=> "POLYtpe"
			case PickleFormat.IMPLICITMETHODtpe	=> "IMPLICITMETHODtpe"
			case PickleFormat.SUPERtpe			=> "SUPERtpe"	 
			case PickleFormat.LITERALunit		=> "LITERALunit"
			case PickleFormat.LITERALboolean 	=> "LITERALboolean"
			case PickleFormat.LITERALbyte		=> "LITERALbyte"
			case PickleFormat.LITERALshort		=> "LITERALshort"
			case PickleFormat.LITERALchar		=> "LITERALchar"
			case PickleFormat.LITERALint		=> "LITERALint"
			case PickleFormat.LITERALlong		=> "LITERALlong"
			case PickleFormat.LITERALfloat		=> "LITERALfloat"
			case PickleFormat.LITERALdouble		=> "LITERALdouble"
			case PickleFormat.LITERALstring		=> "LITERALstring"
			case PickleFormat.LITERALnull		=> "LITERALnull"
			case PickleFormat.LITERALclass		=> "LITERALclass"
			case PickleFormat.LITERALenum		=> "LITERALenum"
			case PickleFormat.SYMANNOT			=> "SYMANNOT"
			case PickleFormat.CHILDREN			=> "CHILDREN"
			case PickleFormat.ANNOTATEDtpe		=> "ANNOTATEDtpe"
			case PickleFormat.ANNOTINFO			=> "ANNOTINFO"
			case PickleFormat.ANNOTARGARRAY		=> "ANNOTARGARRAY"
			// case PickleFormat.DEBRUIJNINDEXtpe => "DEBRUIJNINDEXtpe"
			case PickleFormat.EXISTENTIALtpe 	=> "EXISTENTIALtpe"
			case PickleFormat.TREE				=> "TREE"
			case PickleFormat.MODIFIERS			=> "MODIFIERS"
			case _					=> "***BAD TAG***(" + tag + ")"
		}
	}
	*/
}
