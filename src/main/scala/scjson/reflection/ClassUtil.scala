package scjson.reflection

import java.lang.annotation.Annotation

import scutil.Implicits._

object ClassUtil {
	// TODO use scutil.Lists.unfold???
	
	def inheritanceChain(clazz:Class[_]):List[Class[_]] =
			if (clazz != null)	clazz :: inheritanceChain(clazz.getSuperclass)
			else				Nil
	
	def annotation[T <: Annotation](clazz:Class[_], annotationClass:Class[T]):Option[T] =
			Option(clazz getAnnotation annotationClass)

	/*
	def outerChain(clazz:Class[_]):List[Class[_]]	=
			if (clazz != null)	clazz :: outerChain(clazz.getDeclaringClass)
			else				Nil				
	
		
	def toplevelOuter(clazz:Class[_]):Class[_]	= 
			if (clazz.getDeclaringClass == null)	clazz
			else 									toplevelOuter(clazz.getDeclaringClass)
	*/

			
	//------------------------------------------------------------------------------
	
	def toplevelName(clazz:Class[_]):String			= clazz.getName replaceAll ("[$].*", "")
	// def nameWithoutOuter(clazz:Class[_]):String		= clazz.getName replaceAll (".*[.$]", "")
	def nameWithoutPackage(clazz:Class[_]):String	= clazz.getName replaceAll (".*[.]",  "")
	
	def nameChain(clazz:Class[_]):Seq[String]	= nameWithoutPackage(clazz) splitAround '$'
}
