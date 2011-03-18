package scjson.reflect.scalasignature

import scutil.ext.BooleanImplicits._

import scala.reflect.generic.Flags

object Flag {
	def decodePickled(code:Long):Set[Flag]	=
			decode(Flags pickledToRawFlags code)
		
	def decode(bits:Long):Set[Flag]	= {
		def maybe(mask:Long, flag:Flag):Option[Flag]	= 
				(bits & mask) != 0 guard flag
				
		Set.empty ++
		maybe(Flags.IMPLICIT,		IMPLICIT)		++
		maybe(Flags.FINAL,			FINAL)			++
		maybe(Flags.PRIVATE,		PRIVATE)		++
		maybe(Flags.PROTECTED,		PROTECTED)		++
		maybe(Flags.SEALED,			SEALED)			++
		maybe(Flags.OVERRIDE,		OVERRIDE)		++
		maybe(Flags.CASE,			CASE)			++
		maybe(Flags.ABSTRACT,		ABSTRACT)		++
		maybe(Flags.DEFERRED,		DEFERRED)		++
		maybe(Flags.METHOD,			METHOD)			++
		maybe(Flags.MODULE,			MODULE)			++
		maybe(Flags.INTERFACE,		INTERFACE)		++
		maybe(Flags.MUTABLE,		MUTABLE)		++
		maybe(Flags.PARAM,			PARAM)			++
		maybe(Flags.PACKAGE,		PACKAGE)		++
		maybe(Flags.COVARIANT,		COVARIANT)		++
		maybe(Flags.CAPTURED,		CAPTURED)		++
		maybe(Flags.BYNAMEPARAM,	BYNAMEPARAM)	++
		maybe(Flags.CONTRAVARIANT,	CONTRAVARIANT)	++
		maybe(Flags.LABEL,			LABEL)			++
		maybe(Flags.INCONSTRUCTOR,	INCONSTRUCTOR)	++
		maybe(Flags.ABSOVERRIDE,	ABSOVERRIDE)	++
		maybe(Flags.LOCAL,			LOCAL)			++
		maybe(Flags.JAVA,			JAVA)			++
		maybe(Flags.SYNTHETIC,		SYNTHETIC)		++
		maybe(Flags.STABLE,			STABLE)			++
		maybe(Flags.STATIC,			STATIC)			++
		maybe(Flags.CASEACCESSOR,	CASEACCESSOR)	++
		maybe(Flags.TRAIT,			TRAIT)			++
		maybe(Flags.DEFAULTPARAM,	DEFAULTPARAM)	++
		maybe(Flags.BRIDGE,			BRIDGE)			++
		maybe(Flags.ACCESSOR,		ACCESSOR)		++
		maybe(Flags.SUPERACCESSOR,	SUPERACCESSOR)	++
		maybe(Flags.PARAMACCESSOR,	PARAMACCESSOR)	++
		maybe(Flags.MODULEVAR,		MODULEVAR)		++
		maybe(Flags.SYNTHETICMETH,	SYNTHETICMETH)	++
		maybe(Flags.MONOMORPHIC,	MONOMORPHIC)	++
		maybe(Flags.LAZY,			LAZY)			++
		maybe(Flags.IS_ERROR,		IS_ERROR)		++
		maybe(Flags.OVERLOADED,		OVERLOADED)		++
		maybe(Flags.LIFTED,			LIFTED)			++
		maybe(Flags.MIXEDIN,		MIXEDIN)		++
		maybe(Flags.EXISTENTIAL,	EXISTENTIAL)	++
		maybe(Flags.EXPANDEDNAME,	EXPANDEDNAME)	++
		maybe(Flags.IMPLCLASS,		IMPLCLASS)		++
		maybe(Flags.PRESUPER,		PRESUPER)		++
		maybe(Flags.TRANS_FLAG,		TRANS_FLAG)		++
		maybe(Flags.LOCKED,			LOCKED)			++
		maybe(Flags.SPECIALIZED,	SPECIALIZED)	++
		maybe(Flags.DEFAULTINIT,	DEFAULTINIT)	++
		maybe(Flags.VBRIDGE,		VBRIDGE)
	}
}

sealed abstract class Flag(name:String)
case object IMPLICIT		extends Flag("IMPLICIT")
case object FINAL			extends Flag("FINAL")
case object PRIVATE			extends Flag("PRIVATE")
case object PROTECTED		extends Flag("PROTECTED")
case object SEALED			extends Flag("SEALED")
case object OVERRIDE		extends Flag("OVERRIDE")
case object CASE			extends Flag("CASE")
case object ABSTRACT		extends Flag("ABSTRACT")
case object DEFERRED		extends Flag("DEFERRED")
case object METHOD			extends Flag("METHOD")
case object MODULE			extends Flag("MODULE")
case object INTERFACE		extends Flag("INTERFACE")
case object MUTABLE			extends Flag("MUTABLE")
case object PARAM			extends Flag("PARAM")
case object PACKAGE			extends Flag("PACKAGE")
case object COVARIANT		extends Flag("COVARIANT")
case object CAPTURED		extends Flag("CAPTURED")
case object BYNAMEPARAM		extends Flag("BYNAMEPARAM")
case object CONTRAVARIANT	extends Flag("CONTRAVARIANT")
case object LABEL			extends Flag("LABEL")
case object INCONSTRUCTOR	extends Flag("INCONSTRUCTOR")
case object ABSOVERRIDE		extends Flag("ABSOVERRIDE")
case object LOCAL			extends Flag("LOCAL")
case object JAVA			extends Flag("JAVA")
case object SYNTHETIC		extends Flag("SYNTHETIC")
case object STABLE			extends Flag("STABLE")
case object STATIC			extends Flag("STATIC")
case object CASEACCESSOR	extends Flag("CASEACCESSOR")
case object TRAIT			extends Flag("TRAIT")
case object DEFAULTPARAM	extends Flag("DEFAULTPARAM")
case object BRIDGE			extends Flag("BRIDGE")
case object ACCESSOR		extends Flag("ACCESSOR")
case object SUPERACCESSOR	extends Flag("SUPERACCESSOR")
case object PARAMACCESSOR	extends Flag("PARAMACCESSOR")
case object MODULEVAR		extends Flag("MODULEVAR")
case object SYNTHETICMETH	extends Flag("SYNTHETICMETH")
case object MONOMORPHIC		extends Flag("MONOMORPHIC")
case object LAZY			extends Flag("LAZY")
case object IS_ERROR		extends Flag("IS_ERROR")
case object OVERLOADED		extends Flag("OVERLOADED")
case object LIFTED			extends Flag("LIFTED")
case object MIXEDIN			extends Flag("MIXEDIN")
case object EXISTENTIAL		extends Flag("EXISTENTIAL")
case object EXPANDEDNAME	extends Flag("EXPANDEDNAME")
case object IMPLCLASS		extends Flag("IMPLCLASS")
case object PRESUPER		extends Flag("PRESUPER")
case object TRANS_FLAG		extends Flag("TRANS_FLAG")
case object LOCKED			extends Flag("LOCKED")
case object SPECIALIZED		extends Flag("SPECIALIZED")
case object DEFAULTINIT		extends Flag("DEFAULTINIT")
case object VBRIDGE			extends Flag("VBRIDGE")
