name			:= "scjson"

organization	:= "de.djini"

version			:= "0.30.0"

scalaVersion	:= "2.10.2"

libraryDependencies	++= Seq(
	"de.djini"			%%	"scutil"		% "0.27.0"				% "compile",
	"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
	"org.specs2"		%%	"specs2"		% "2.2"					% "test"
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
