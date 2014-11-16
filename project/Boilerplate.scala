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
		val outFile	= outDir / "TupleProtocolGenerated.scala"
		IO write (outFile,	genTupleTrait)
		outFile
	}
	
	def genTupleTrait:String	= {
		"""
		|package scjson.serialization
		|
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
		val typeParams	= awc("T$:Format") 
		val typeNames	= awc("T$")
		("""
		|	implicit def Tuple"""+arity+"""Format["""+typeParams+"""]:Format[("""+typeNames+""")]	=
		|			Format[("""+typeNames+""")](
		|				(out:("""+typeNames+"""))	=> {
		|					JSONVarArray("""+ awc("doWrite[T$](out._$)")	+""")
		|				},
		|				(in:JSONValue)	=> {
		|					val	arr	= arrayValue(in)
		|					("""+ awc("doRead[T$](arr($-1))") +	""")
		|				}
		|			)
		""").stripMargin
	}
	
	//------------------------------------------------------------------------------
	//## case classes
	
	def genCaseClassFile(outDir:File):File	= {
		val outFile	= outDir / "CaseClassProtocolGenerated.scala"
		IO write (outFile,	genCaseClassTrait)
		outFile
	}
	
	def genCaseClassTrait:String	= {
		"""
		|package scjson.serialization
		|
		|import scutil.lang.Fielder
		|import scutil.lang.Fielding
		|import scutil.lang.ISeq
		|import scjson._
		|import JSONSerializationUtil._
		|
		|trait CaseClassProtocolGenerated {
		""".stripMargin		+ 
		(2 to 22 map genCaseClassMethod mkString "\n")	+
		"""
		|}
		""".stripMargin
	}
	
	def genCaseClassMethod(arity:Int):String	= {
		val awc	= aritywise(arity)(",") _
		val typeParams	= awc("S$:Format") 
		val typeNames	= awc("S$")
		val fieldNames	= awc("k$")
		("""
		|	def caseClassFormat"""+arity+"""["""+typeParams+""",T:Fielding](apply:("""+typeNames+""")=>T, unapply:T=>Option[("""+typeNames+""")]):Format[T]	= {
		|		val ISeq("""+fieldNames+""")	= Fielder[T]
		|		Format[T](
		|			(out:T)	=> {
		|				val fields	= unapply(out).get
		|				JSONVarObject(""" + awc("k$ -> doWrite[S$](fields._$)") + """)
		|			},
		|			(in:JSONValue)	=> {
		|				val map	= objectMap(in)
		|				apply(""" + awc("doRead[S$](map(k$))") + """)
		|			}
		|		)
		|	}
		""").stripMargin
	}
	
	//------------------------------------------------------------------------------
	//## helper
	
	private def aritywise(arity:Int)(separator:String)(format:String):String	= 
			1 to arity map { i => format replace ("$", i.toString) } mkString separator
}
