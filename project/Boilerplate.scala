import sbt._

object Boilerplate {
	def generate(srcDir:File):Seq[File]	= {
		val	outDir	= srcDir / "boilerplate"
		outDir.mkdirs()
		Seq(
			genTupleFile(outDir), 
			genCaseClassFile(outDir)
		)
	}	
	
	//------------------------------------------------------------------------------
	//## tuples
	
	def genTupleFile(outDir:File):File	= {
		val outFile		= outDir / "TupleProtocolGenerated.scala"
		IO write (outFile,	genTupleTrait)
		outFile
	}
	
	def genTupleTrait:String	= {
		"""
		|package scjson.serialization
		|
		|import scutil.Implicits._
		|import scjson._
		|import JSONSerializationUtil._
		|
		|trait TupleProtocolGenerated {
		""".stripMargin		+ 
		(2 to 22 map genTupleMethod mkString "\n")	+
		"""
		|}
		""".stripMargin
	}
			
	def genTupleMethod(arity:Int):String	= {
		val awc	= aritywise(arity)(",") _
		val typeParams	= awc("T$:JSONFormat") 
		val typeNames	= awc("T$")
		("""
		|	implicit def Tuple"""+arity+"""JSONFormat["""+typeParams+"""]:JSONFormat[("""+typeNames+""")]	= new JSONFormat[("""+typeNames+""")] {
		|		def write(out:("""+typeNames+""")):JSONValue	= {
		|			JSONArray(Seq("""+ awc("doWrite[T$](out._$)")	+"""))
		|		}
		|		def read(in:JSONValue):("""+typeNames+""")	= {
		|			val	arr	= arrayValue(in)
		|			("""+ awc("doRead[T$](arr($-1))") +	""")
		|		}
		|	}
		""").stripMargin
	}
	
	//------------------------------------------------------------------------------
	//## case classes
	
	def genCaseClassFile(outDir:File):File	= {
		val outFile		= outDir / "CaseClassProtocolGenerated.scala"
		IO write (outFile,	genCaseClassTrait)
		outFile
	}
	
	def genCaseClassTrait:String	= {
		"""
		|package scjson.serialization
		|
		|import reflect.runtime.universe._
		|import scutil.Implicits._
		|import scjson._
		|import JSONSerializationUtil._
		|
		|trait CaseClassProtocolGenerated {
		""".stripMargin		+ 
		(2 to 22 map genCaseClassMethod mkString "\n")	+
		"""
		|	protected def fieldNamesFor[T:TypeTag]:Seq[String]
		|}
		""".stripMargin
	}
	
	def genCaseClassMethod(arity:Int):String	= {
		val awc	= aritywise(arity)(",") _
		val typeParams	= awc("S$:JSONFormat") 
		val typeNames	= awc("S$")
		val fieldNames	= awc("k$")
		("""
		|	def caseClassJSONFormat"""+arity+"""["""+typeParams+""",T:TypeTag](apply:("""+typeNames+""")=>T, unapply:T=>Option[("""+typeNames+""")]):JSONFormat[T]	= {
		|		val Seq("""+fieldNames+""")	= fieldNamesFor[T]
		|		new JSONFormat[T] {
		|			def write(out:T):JSONValue	= {
		|				val fields	= unapply(out).get
		|				JSONObject(Seq(""" + awc("k$ -> doWrite[S$](fields._$)") + """))
		|			}
		|			def read(in:JSONValue):T	= {
		|				val map	= objectMap(in)
		|				apply(""" + awc("doRead[S$](map(k$))") + """)
		|			}
		|		}
		|	}
		""").stripMargin
	}
	
	//------------------------------------------------------------------------------
	//## helper
	
	private def aritywise(arity:Int)(separator:String)(format:String):String	= 
			1 to arity map { i => format replace ("$", i.toString) } mkString separator
}
