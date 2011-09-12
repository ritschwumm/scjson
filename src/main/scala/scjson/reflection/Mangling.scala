package scjson.reflection

object Mangling {
	def mangle(s:String):String		= operators.foldLeft(s) { (n, o) => n.replace (o._1, o._2) }
	def unmangle(s:String):String	= operators.foldLeft(s) { (n, o) => n.replace (o._2, o._1) }

	private val operators = Set(
		"="		-> "$eq",
		">"		-> "$greater", 
		"<"		-> "$less", 
		"+"		-> "$plus", 
		"-"		-> "$minus",
		"*"		-> "$times", 
		"/"		-> "$div", 
		"!"		-> "$bang", 
		"@"		-> "$at", 
		"#"		-> "$hash",
		"%"		-> "$percent", 
		"^"		-> "$up", 
		"&"		-> "$amp", 
		"~"		-> "$tilde", 
		"?"		-> "$qmark",
		"|"		-> "$bar", 
		"\\"	-> "$bslash"
	)
}
