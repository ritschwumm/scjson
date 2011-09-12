package scjson.reflection

import scutil.Implicits._

import scjson.reflection.scalasignature._

object Reflector {
	import Decoder._
			
	/** unmangled constructor parameter names */
	def constructor(clazz:Class[_]):Option[Seq[String]]	= {
		// it's ok to have no ctor for an object!
		if (clazz.getName endsWith "$")	return Some(Seq.empty)
		
		// for a constructor we always need a signature
		val (signature,classSyms)	= findClassSyms(clazz) getOrError "no signature found"
		val params					= ctorsParams(signature, classSyms)
		params.singleOption map { _ map Mangling.unmangle }
	}
	
	/** unmangled data accessor names */
	def accessors(clazz:Class[_]):Seq[String]	=
			for {
				clazz					<- ClassUtil inheritanceChain clazz
				// for accessors we simply skip classes without a signature
				(signature, classSyms)	<- findClassSyms(clazz).toSeq
				accessor				<- dataAccessors(signature, classSyms)
			}
			yield (Mangling unmangle accessor)
	
	//------------------------------------------------------------------------------
	
	/** finds both class and companion */
	private def findClassSyms(clazz:Class[_]):Option[(Signature,Seq[CLASSsym])]	=
			findSignature(clazz) map { signature =>
				def recurse(owner:Option[Ref], nameChain:Seq[String]):Seq[CLASSsym]	= {
					val candidates	= 
							for {
								it@CLASSsym(classRef, SymbolInfo(className, classOwner, classFlags, classPrivateWithin, classInfo), classThistype)	<- signature.entries
								if owner.isEmpty || owner == Some(classOwner)
								TYPEname(`className`, NameInfo(typeName))																			<- signature.entries
								if typeName == nameChain.head
							}
							yield it
					if (nameChain.size > 1) {
						for {
							parent	<- candidates
							child	<- recurse(Some(parent.self), nameChain.tail)
						}
						yield child
					}
					else {
						candidates
					}
				}
				val classSyms	= recurse(None, ClassUtil nameChain clazz)
				(signature, classSyms)
			}
			
	/** signature is on the toplevel outer class */
	private def findSignature(clazz:Class[_]):Option[Signature]	= {
		val topName		= ClassUtil toplevelName clazz
		val topClass	= Class forName (topName, true, clazz.getClassLoader)
		Decoder decode topClass
	}
	
	//------------------------------------------------------------------------------
	
	private def ctorsParams(signature:Signature, classSyms:Seq[CLASSsym]):Seq[Seq[String]] = {
		val symSeqs:Seq[Seq[Ref]]	= 
				for {
					CLASSsym(classRef, SymbolInfo(className, classOwner, classFlags, classPrivateWithin, classInfo), classThistype)	<- classSyms
					// restricted to case classes, explicitly excluding companion objects
					if (classFlags contains CASE) && !(classFlags contains MODULE)	
					VALsym(ctorValRef, SymbolInfo(name, `classRef`, flags, privateWithin, info), alias)								<- signature.entries
					TERMname(`name`, NameInfo("<init>"))																			<- signature.entries
					METHODtpe(`info`, result, params)																				<- signature.entries
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
	
	private def dataAccessors(signature:Signature, classSyms:Seq[CLASSsym]):Seq[String] =
			for {
				CLASSsym(classRef, SymbolInfo(className, classOwner, classFlags, classPrivateWithin, classInfo), classThistype)	<- classSyms
				if !(classFlags contains MODULE)
				// no restriction to case classes, data accessors are allowed anywhere in the inheritance chain
				VALsym(accessorValRef, SymbolInfo(nameRef, `classRef`, flags, privateWithin, info), alias)						<- signature.entries
				// restricted to public accessors
				if (flags containsAll Set(METHOD, STABLE, ACCESSOR)) && (flags containsNone Set(PRIVATE, LOCAL)) 
				TERMname(`nameRef`, NameInfo(name))																				<- signature.entries
			} 
			yield name
}
