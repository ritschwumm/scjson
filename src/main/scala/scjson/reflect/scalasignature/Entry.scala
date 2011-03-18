package scjson.reflect.scalasignature

// @see scala.tools.nsc.symtab.classfile.Pickler

/*
Symbol table attribute format:
Symtab        = nentries_Nat {Entry}
Entry         = 1 TERMNAME len_Nat NameInfo
			  | 2 TYPENAME len_Nat NameInfo
			  | 3 NONEsym len_Nat
			  | 4 TYPEsym len_Nat SymbolInfo
			  | 5 ALIASsym len_Nat SymbolInfo
			  | 6 CLASSsym len_Nat SymbolInfo [thistype_Ref]
			  | 7 MODULEsym len_Nat SymbolInfo
			  | 8 VALsym len_Nat [defaultGetter_Ref] SymbolInfo [alias_Ref]	//### defaultGetter_Ref is no longer needed
			  | 9 EXTref len_Nat name_Ref [owner_Ref]
			  | 10 EXTMODCLASSref len_Nat name_Ref [owner_Ref]
			  | 11 NOtpe len_Nat
			  | 12 NOPREFIXtpe len_Nat
			  | 13 THIStpe len_Nat sym_Ref
			  | 14 SINGLEtpe len_Nat type_Ref sym_Ref
			  | 15 CONSTANTtpe len_Nat constant_Ref
			  | 16 TYPEREFtpe len_Nat type_Ref sym_Ref {targ_Ref}
			  | 17 TYPEBOUNDStpe len_Nat tpe_Ref tpe_Ref
			  | 18 REFINEDtpe len_Nat classsym_Ref {tpe_Ref}
			  | 19 CLASSINFOtpe len_Nat classsym_Ref {tpe_Ref}
			  | 20 METHODtpe len_Nat tpe_Ref {sym_Ref}
			  | 21 POLYTtpe len_Nat tpe_Ref {sym_Ref}
			  | 22 IMPLICITMETHODtpe len_Nat tpe_Ref {sym_Ref} //### no longer needed
			  | 52 SUPERtpe len_Nat tpe_Ref tpe_Ref
			  | 24 LITERALunit len_Nat
			  | 25 LITERALboolean len_Nat value_Long
			  | 26 LITERALbyte len_Nat value_Long
			  | 27 LITERALshort len_Nat value_Long
			  | 28 LITERALchar len_Nat value_Long
			  | 29 LITERALint len_Nat value_Long
			  | 30 LITERALlong len_Nat value_Long
			  | 31 LITERALfloat len_Nat value_Long
			  | 32 LITERALdouble len_Nat value_Long
			  | 33 LITERALstring len_Nat name_Ref
			  | 34 LITERALnull len_Nat
			  | 35 LITERALclass len_Nat tpe_Ref
			  | 36 LITERALenum len_Nat sym_Ref
			  | 40 SYMANNOT len_Nat sym_Ref AnnotInfoBody
			  | 41 CHILDREN len_Nat sym_Ref {sym_Ref}
			  | 42 ANNOTATEDtpe len_Nat [sym_Ref] tpe_Ref {annotinfo_Ref}	//### sym_Ref is no longer neede
			  | 43 ANNOTINFO len_Nat AnnotInfoBody
			  | 44 ANNOTARGARRAY len_Nat {constAnnotArg_Ref}
			  | 47 DEBRUIJNINDEXtpe len_Nat level_Nat index_Nat
			  | 48 EXISTENTIALtpe len_Nat type_Ref {symbol_Ref}
			  | 49 TREE len_Nat 1 EMPTYtree
			  | 49 TREE len_Nat 2 PACKAGEtree type_Ref sym_Ref mods_Ref name_Ref {tree_Ref}
			  | 49 TREE len_Nat 3 CLASStree type_Ref sym_Ref mods_Ref name_Ref tree_Ref {tree_Ref}
			  | 49 TREE len_Nat 4 MODULEtree type_Ref sym_Ref mods_Ref name_Ref tree_Ref
			  | 49 TREE len_Nat 5 VALDEFtree type_Ref sym_Ref mods_Ref name_Ref tree_Ref tree_Ref
			  | 49 TREE len_Nat 6 DEFDEFtree type_Ref sym_Ref mods_Ref name_Ref numtparams_Nat {tree_Ref} numparamss_Nat {numparams_Nat {tree_Ref}} tree_Ref tree_Ref
			  | 49 TREE len_Nat 7 TYPEDEFtree type_Ref sym_Ref mods_Ref name_Ref tree_Ref {tree_Ref}
			  | 49 TREE len_Nat 8 LABELtree type_Ref sym_Ref tree_Ref {tree_Ref}
			  | 49 TREE len_Nat 9 IMPORTtree type_Ref sym_Ref tree_Ref {name_Ref name_Ref}
			  | 49 TREE len_Nat 11 DOCDEFtree type_Ref sym_Ref string_Ref tree_Ref
			  | 49 TREE len_Nat 12 TEMPLATEtree type_Ref sym_Ref numparents_Nat {tree_Ref} tree_Ref {tree_Ref}
			  | 49 TREE len_Nat 13 BLOCKtree type_Ref tree_Ref {tree_Ref}
			  | 49 TREE len_Nat 14 CASEtree type_Ref tree_Ref tree_Ref tree_Ref
			  | 49 TREE len_Nat 15 SEQUENCEtree type_Ref {tree_Ref}
			  | 49 TREE len_Nat 16 ALTERNATIVEtree type_Ref {tree_Ref}
			  | 49 TREE len_Nat 17 STARtree type_Ref {tree_Ref}
			  | 49 TREE len_Nat 18 BINDtree type_Ref sym_Ref name_Ref tree_Ref
			  | 49 TREE len_Nat 19 UNAPPLYtree type_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 20 ARRAYVALUEtree type_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 21 FUNCTIONtree type_Ref sym_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 22 ASSIGNtree type_Ref tree_Ref tree_Ref 
			  | 49 TREE len_Nat 23 IFtree type_Ref tree_Ref tree_Ref tree_Ref 
			  | 49 TREE len_Nat 24 MATCHtree type_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 25 RETURNtree type_Ref sym_Ref tree_Ref
			  | 49 TREE len_Nat 26 TREtree type_Ref tree_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 27 THROWtree type_Ref tree_Ref 
			  | 49 TREE len_Nat 28 NEWtree type_Ref tree_Ref 
			  | 49 TREE len_Nat 29 TYPEDtree type_Ref tree_Ref tree_Ref 
			  | 49 TREE len_Nat 30 TYPEAPPLYtree type_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 31 APPLYtree type_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 32 APPLYDYNAMICtree type_Ref sym_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 33 SUPERtree type_Ref sym_Ref tree_Ref name_Ref
			  | 49 TREE len_Nat 34 THIStree type_Ref sym_Ref  name_Ref
			  | 49 TREE len_Nat 35 SELECTtree type_Ref sym_Ref tree_Ref name_Ref
			  | 49 TREE len_Nat 36 IDENTtree type_Ref sym_Ref name_Ref
			  | 49 TREE len_Nat 37 LITERALtree type_Ref constant_Ref 
			  | 49 TREE len_Nat 38 TYPEtree type_Ref 
			  | 49 TREE len_Nat 39 ANNOTATEDtree type_Ref tree_Ref tree_Ref
			  | 49 TREE len_Nat 40 SINGLETONTYPEtree type_Ref tree_Ref 
			  | 49 TREE len_Nat 41 SELECTFROMTYPEtree type_Ref tree_Ref name_Ref 
			  | 49 TREE len_Nat 42 COMPOUNDTYPEtree type_Ref tree_Ref 
			  | 49 TREE len_Nat 43 APPLIEDTYPEtree type_Ref tree_Ref {tree_Ref} 
			  | 49 TREE len_Nat 44 TYPEBOUNDStree type_Ref tree_Ref tree_Ref
			  | 49 TREE len_Nat 45 EXISTENTIALTYPEtree type_Ref tree_Ref {tree_Ref} 
			  | 50 MODIFIERS len_Nat flags_Long privateWithin_Ref
SymbolInfo    = name_Ref owner_Ref flags_LongNat [privateWithin_Ref] info_Ref
NameInfo      = <character sequence of length len_Nat in Utf8 format>
NumInfo       = <len_Nat-byte signed number in big endian format>
Ref           = Nat
AnnotInfoBody = info_Ref {annotArg_Ref} {name_Ref constAnnotArg_Ref}
AnnotArg      = Tree | Constant
ConstAnnotArg = Constant | AnnotInfo | AnnotArgArray

len is remaining length after `len'.
*/

sealed abstract class Entry {
	val self:Ref
}
// TODO hack
case class UNKNOWN(self:Ref, tag:Int, body:Array[Byte])													extends Entry

case class TERMname(self:Ref, nameInfo:NameInfo)														extends Entry
case class TYPEname(self:Ref, nameInfo:NameInfo)														extends Entry
case class NONEsym(self:Ref)																			extends Entry
case class TYPEsym(self:Ref, symbolInfo:SymbolInfo)														extends Entry
case class ALIASsym(self:Ref, symbolInfo:SymbolInfo)													extends Entry
case class CLASSsym(self:Ref, symbolInfo:SymbolInfo, thistype_t:Option[Ref])							extends Entry
case class MODULEsym(self:Ref, symbolInfo:SymbolInfo)													extends Entry
// NOTE defaultGetter_Ref is no longer written by the Pickler
case class VALsym(self:Ref, /*defaultGetter:Option[Ref], */ symbolInfo:SymbolInfo, alias_s:Option[Ref])	extends Entry

case class EXTref(self:Ref, name_n:Ref, owner_s:Option[Ref])											extends Entry
case class EXTMODCLASSref(self:Ref, name_n:Ref, owner_s:Option[Ref])									extends Entry

case class NOtpe(self:Ref)																				extends Entry
case class NOPREFIXtpe(self:Ref)																		extends Entry
case class THIStpe(self:Ref, sym_s:Ref)																	extends Entry
case class SINGLEtpe(self:Ref, pre_t:Ref, sym_s:Ref)													extends Entry
case class CONSTANTtpe(self:Ref, value_c:Ref)															extends Entry
case class TYPEREFtpe(self:Ref, pre_t:Ref, sym_s:Ref, arg_t:Seq[Ref])									extends Entry
case class TYPEBOUNDStpe(self:Ref, lo_t:Ref, hi_t:Ref)													extends Entry
// TODO this are two sequences, not one!
case class REFINEDtpe(self:Ref, class_s:Ref, parent_t:Seq[Ref]/*, decl_s:Seq[Ref]*/)					extends Entry
// TODO this are two sequences, not one!
case class CLASSINFOtpe(self:Ref, class_s:Ref, parent_t:Seq[Ref]/*, decl_s:Seq[Ref]*/)					extends Entry
case class METHODtpe(self:Ref, result_t:Ref, param_s:Seq[Ref])											extends Entry
case class POLYtpe(self:Ref, result_t:Ref, tparam_s:Seq[Ref])											extends Entry
// NOTE IMPLICITMETHODtpe is no longer written by the Pickler
case class IMPLICITMETHODtpe(self:Ref, tpe:Ref, sym:Seq[Ref])											extends Entry
case class SUPERtpe(self:Ref, thistpe:Ref, supertpe:Ref)												extends Entry

case class LITERALunit(self:Ref)																		extends Entry
case class LITERALboolean(self:Ref, value:Long)															extends Entry
case class LITERALbyte(self:Ref, value:Long)															extends Entry
case class LITERALshort(self:Ref, value:Long)															extends Entry
case class LITERALchar(self:Ref, value:Long)															extends Entry
case class LITERALint(self:Ref, value:Long)																extends Entry
case class LITERALlong(self:Ref, value:Long)															extends Entry
case class LITERALfloat(self:Ref, value:Long)															extends Entry
case class LITERALdouble(self:Ref, value:Long)															extends Entry
case class LITERALstring(self:Ref, name:Ref)															extends Entry
case class LITERALnull(self:Ref)																		extends Entry
case class LITERALclass(self:Ref, tpe:Ref)																extends Entry
case class LITERALenum(self:Ref, sym:Ref)																extends Entry

case class SYMANNOT(self:Ref, sym_s:Ref, annotInfoBody:AnnotInfoBody)									extends Entry
case class CHILDREN(self:Ref, sym_s:Ref, children_s:Seq[Ref])											extends Entry
case class ANNOTATEDtpe(self:Ref, underlying_t:Ref, self_s:Option[Ref], annotinfo:Seq[Ref])				extends Entry
case class ANNOTINFO(self:Ref, annotInfoBody:AnnotInfoBody)												extends Entry
case class ANNOTARGARRAY(self:Ref, constAnnotArg:Seq[Ref])												extends Entry
case class DEBRUIJNINDEXtpe(self:Ref, level:Int, index:Int)												extends Entry
case class EXISTENTIALtpe(self:Ref, result_t:Ref, tparam_s:Seq[Ref])									extends Entry
case class TREE(self:Ref, tree:XTree) 																	extends Entry
case class MODIFIERS(self:Ref, flags:Long, privateWithin:Ref)											extends Entry
		
// named XTree to avoid TREE.class clashing with Tree.class on OS X
sealed abstract class XTree
// TODO implement
case object DummyTree extends XTree
/*
case object EMPTYtree extends Tree
case class PACKAGEtree(typee:Ref, sym:Ref, mods:Ref, name:Ref, tree:Seq[Ref])				extends XTree
case class CLASStree(typee:Ref, sym:Ref, mods:Ref, name:Ref, tree:Ref, tree1:Seq[Ref])		extends XTree
case class MODULEtree(typee:Ref, sym:Ref, mods:Ref, name:Ref, tree:Ref)						extends XTree
case class VALDEFtree(typee:Ref, sym:Ref, mods:Ref, name:Ref, tree:Ref, tree1:Ref)			extends XTree
case class DEFDEFtree(typee:Ref, sym:Ref, mods:Ref, name:Ref, tparams:Seq[Ref], paramss:Seq[Seq[Ref]], tree1:Ref, tree2:Ref)	extends XTree
case class TYPEDEFtree(typee:Ref, sym:Ref, mods:Ref, name:Ref, tree:Ref, tree1:Seq[Ref])	extends XTree
case class LABELtree(typee:Ref, sym:Ref, tree:Ref, tree1:Seq[Ref])							extends XTree
case class IMPORTtree(typee:Ref, sym:Ref, tree:Ref, name:Seq[Pair[Ref,Ref]])				extends XTree
case class DOCDEFtree(typee:Ref, sym:Ref, string:Ref, tree:Ref)								extends XTree
case class TEMPLATEtree(typee:Ref, sym:Ref, parents:Seq[Ref], tree1:Ref, tree2:Seq[Ref])	extends XTree
case class BLOCKtree(typee:Ref, tree1:Ref, tree2:Seq[Ref])									extends XTree
case class CASEtree(typee:Ref, tree1:Ref, tree2:Ref, tree3:Ref)								extends XTree
case class SEQUENCEtree(typee:Ref, tree:Seq[Ref])											extends XTree
case class ALTERNATIVEtree(typee:Ref, tree:Seq[Ref])										extends XTree
case class STARtree(typee:Ref, tree:Seq[Ref])												extends XTree
case class BINDtree(typee:Ref, sym:Ref, name:Ref, tree:Ref)									extends XTree
case class UNAPPLYtree(typee:Ref, sym:Ref, tree1:Ref, tree2:Seq[Ref])						extends XTree
case class ARRAYVALUEtree(typee:Ref, tree1:Ref, tree2:Seq[Ref])								extends XTree
case class FUNCTIONtree(typee:Ref, sym:Ref, tree1:Ref, tree2:Seq[Ref])						extends XTree
case class ASSIGNtree(typee:Ref, tree1:Ref, tree2:Ref)										extends XTree
case class IFtree(typee:Ref, tree1:Ref, tree2:Ref, tree3:Ref)								extends XTree
case class MATCHtree(typee:Ref, tree1:Ref, tree2:Seq[Ref])									extends XTree
case class RETURNtree(typee:Ref, sym:Ref, tree:Ref)											extends XTree
case class TREtree(typee:Ref, tree1:Ref, tree2:Ref, tree3:Seq[Ref])							extends XTree
case class THROWtree(typee:Ref, tree:Ref)													extends XTree
case class NEWtree(typee:Ref, tree:Ref)														extends XTree
case class TYPEDtree(typee:Ref, tree1:Ref, tree2:Ref)										extends XTree
case class TYPEAPPLYtree(typee:Ref, tree1:Ref, tree2:Seq[Ref])								extends XTree
case class APPLYtree(typee:Ref, tree1:Ref, tree2:Seq[Ref])									extends XTree
case class APPLYDYNAMICtree(typee:Ref, sym:Ref, tree1:Ref, tree2:Seq[Ref])					extends XTree
case class SUPERtree(typee:Ref, sym:Ref, tree:Ref, name:Ref)								extends XTree
case class THIStree(typee:Ref, sym:Ref, name:Ref)											extends XTree
case class SELECTtree(typee:Ref, sym:Ref, tree:Ref, name:Ref)								extends XTree
case class IDENTtree(typee:Ref, sym:Ref, name:Ref)											extends XTree
case class LITERALtree(typee:Ref, constant:Ref)												extends XTree
case class TYPEtree(typee:Ref)																extends XTree
case class ANNOTATEDtree(typee:Ref, tree1:Ref, tree2:Ref)									extends XTree
case class SINGLETONTYPEtree(typee:Ref, tree:Ref)											extends XTree
case class SELECTFROMTYPEtree(typee:Ref, tree:Ref, name:Ref)								extends XTree
case class COMPOUNDTYPEtree(typee:Ref, tree:Ref)											extends XTree
case class APPLIEDTYPEtree(typee:Ref, tree1:Ref, tree2:Seq[Ref])							extends XTree
case class TYPEBOUNDStree(typee:Ref, tree1:Ref, tree2:Ref)									extends XTree
case class EXISTENTIALTYPEtree(typee:Ref, tree1:Ref, tree2:Seq[Ref])						extends XTree
*/

case class SymbolInfo(name:Ref, owner_s:Ref, flags:Set[Flag], privateWithin_s:Option[Ref], info_t:Ref)
case class NameInfo(name:String)
// NOTE NumInfo is not used anymore
case class AnnotInfoBody(annotArg:Seq[Ref], const:Seq[Pair[Ref,Ref]])
case class Ref(index:Int)
