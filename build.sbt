name			:= "scjson"

organization	:= "de.djini"

version			:= "0.52.0"

scalaVersion	:= "2.11.2"

libraryDependencies	++= Seq(
	"de.djini"			%%	"scutil-core"	% "0.47.0"				% "compile",
	"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
	"org.specs2"		%%	"specs2"		% "2.3.13"				% "test"		exclude("org.scala-lang", "scala-library")	exclude("org.scala-lang", "scala-reflect")
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
