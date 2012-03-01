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
		|import scmirror._
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
		def aritywise(item:Int=>String):String	= 1 to arity map item mkString ","
		val typeParams	= aritywise("T"+_+":JSONFormat") 
		val typeNames	= aritywise("T"+_)
		("""
		|	implicit def Tuple"""+arity+"""JSONFormat["""+typeParams+"""]:JSONFormat[("""+typeNames+""")]	= new JSONFormat[("""+typeNames+""")] {
		|		def write(out:("""+typeNames+""")):JSONValue	= {
		|			JSONArray(Seq("""+ aritywise(i => "doWrite[T"+i+"](out._"+i+")")	+"""))
		|		}
		|		def read(in:JSONValue):("""+typeNames+""")	= {
		|			val	arr	= arrayValue(in)
		|			("""+ aritywise(i => "doRead[T"+i+"](arr("+(i-1)+"))") +""")
		|		}
		|	}
		""").stripMargin
	}
	
	//------------------------------------------------------------------------------
	
	def genCaseClassFile(outDir:File):File	= {
		val outFile		= outDir / "CaseClassProtocolGenerated.scala"
		IO write (outFile,	genCaseClassTrait)
		outFile
	}
	
	def genCaseClassTrait:String	= {
		"""
		|package scjson.serialization
		|
		|import scutil.Implicits._
		|import scmirror._
		|import scjson._
		|import JSONSerializationUtil._
		|
		|trait CaseClassProtocolGenerated {
		""".stripMargin		+ 
		(2 to 22 map genCaseClassMethod mkString "\n")	+
		"""
		|	protected def fieldNamesFor[T:Manifest]:Seq[String]
		|}
		""".stripMargin
	}
	
	def genCaseClassMethod(arity:Int):String	= {
		def aritywise(item:Int=>String):String	= 1 to arity map item mkString ","
		val typeParams	= aritywise("S"+_+":JSONFormat") 
		val typeNames	= aritywise("S"+_)
		val fieldNames	= aritywise("k"+_)
		("""
		|	def caseClassJSONFormat"""+arity+"""["""+typeParams+""",T:Manifest](apply:("""+typeNames+""")=>T, unapply:T=>Option[("""+typeNames+""")]):JSONFormat[T]	= {
		|		val Seq("""+fieldNames+""")	= fieldNamesFor[T]
		|		new JSONFormat[T] {
		|			def write(out:T):JSONValue	= {
		|				val fields	= unapply(out).get
		|				JSONObject(Map(""" + aritywise(i => "JSONString(k"+i+") -> doWrite[S"+i+"](fields._"+i+")") + """))
		|			}
		|			def read(in:JSONValue):T	= {
		|				val map	= objectValue(in)
		|				apply(""" + aritywise(i => "doRead[S"+i+"](map(JSONString(k"+i+")))") + """)
		|			}
		|		}
		|	}
		""").stripMargin
	}
}
