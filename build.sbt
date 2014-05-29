name			:= "scjson"

organization	:= "de.djini"

version			:= "0.49.0"

scalaVersion	:= "2.11.0"

libraryDependencies	++= Seq(
	"de.djini"			%%	"scutil-core"	% "0.44.0"				% "compile",
	"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
	"org.specs2"		%%	"specs2"		% "2.3.11"				% "test"		exclude("org.scala-lang", "scala-library")	exclude("org.scala-lang", "scala-reflect")
)

scalacOptions	++= Seq(
	"-deprecation",
	"-unchecked",
	"-language:implicitConversions",
	"-language:existentials",
	// "-language:higherKinds",
	// "-language:reflectiveCalls",
	// "-language:dynamics",
	"-language:postfixOps",
	// "-language:experimental.macros"
	"-feature"
)

(sourceGenerators in Compile)	<+= (sourceManaged in Compile) map Boilerplate.generate
