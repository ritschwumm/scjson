name			:= "scjson"

organization	:= "de.djini"

version			:= "0.7.0"

scalaVersion	:= "2.9.2"

libraryDependencies	++= Seq(
	"de.djini"		%%	"scutil"	% "0.7.0"	% "compile",
	"de.djini"		%%	"scmirror"	% "0.3.0"	% "compile",
	"org.specs2"	%%	"specs2"	% "1.11"	% "test"
)

scalacOptions	++= Seq("-deprecation", "-unchecked")

(sourceGenerators in Compile)	<+= (sourceManaged in Compile) map Boilerplate.generate
