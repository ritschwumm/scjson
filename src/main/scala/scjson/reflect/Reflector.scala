package scjson.reflect

import scutil.ext.OptionImplicits._
import scutil.ext.SetImplicits._

import scjson.reflect.scalasignature._

object Reflector {
	import Decoder._
	
	def reflect(clazz:Class[_]):Option[Reflected]	=  {
		// it's ok to have no ctor for an object!
		val	incoming:Option[Seq[String]]	= 
				if (clazz.getName endsWith "$")	Some(Seq.empty)
				else							Decoder decode clazz map ctorsParams map { _.headOption } flatten;
		
		val outgoing:List[String]	= ClassUtil inheritance clazz flatMap Decoder.decode flatMap dataAccessors
		
		for { incoming	<- incoming }
		yield Reflected(
				incoming map Property.fromMangled, 
				outgoing map Property.fromMangled)
	}
	
	private def ctorsParams(signature:Signature):Seq[Seq[String]] = {
		val symSeqs:Seq[Seq[Ref]]	= 
				for {
					CLASSsym(classRef, SymbolInfo(className, classOwner, classFlags, classPrivateWithin, classInfo), classThistype)	<- signature.entries
					// restricted to case classes, explicitly excluding companion objects
					if (classFlags contains CASE) && !(classFlags contains MODULE)	
					VALsym(ctorValRef, SymbolInfo(name, `classRef`, flags, privateWithin, info), alias)								<- signature.entries
					TERMname(`name`, NameInfo("<init>"))																			<- signature.entries
					METHODtpe(`info`, result, params)																						<- signature.entries
				} 
				yield params
		
		def paramNames(symSeq:Seq[Ref]) =
				for {
					VALsym(paramValRef, SymbolInfo(name, owner, flags, privateWithin, tpe), alias)	<- symSeq map signature.deref
					TERMname(`name`, NameInfo(paramName))											<- signature.entries
				}
				yield paramName
				
		symSeqs map paramNames
	}
	
	private def dataAccessors(signature:Signature):Seq[String] =
			for {
				CLASSsym(classRef, SymbolInfo(className, classOwner, classFlags, classPrivateWithin, classInfo), classThistype)	<- signature.entries
				if !(classFlags contains MODULE)
				// no restriction, data accessors are allowed anywhere in the inheritance chain
				VALsym(accessorValRef, SymbolInfo(nameRef, `classRef`, flags, privateWithin, info), alias)						<- signature.entries
				// restricted to public accessors
				if (flags containsAll Set(METHOD, STABLE, ACCESSOR)) && (flags containsNone Set(PRIVATE, LOCAL)) 
				TERMname(`nameRef`, NameInfo(name))																				<- signature.entries
			} 
			yield name
			// println("className", signature deref className)
}
