package scjson.reflect

import java.lang.annotation.Annotation

object ClassUtil {
	def inheritance(clazz:Class[_]):List[Class[_]] =
			clazz :: (clazz.getSuperclass match {
				case null	=> Nil
				case sup	=> inheritance(sup)
			})
			
	def annotation[T <: Annotation](clazz:Class[_], annotationClass:Class[T]):Option[T] =
			Option(clazz getAnnotation annotationClass)
}
