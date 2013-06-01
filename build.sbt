name			:= "scjson"

organization	:= "de.djini"

version			:= "0.22.0"

scalaVersion	:= "2.10.1"

libraryDependencies	++= Seq(
	"de.djini"		%%	"scutil"	% "0.20.0"	% "compile",
	"org.specs2"	%%	"specs2"	% "1.14"	% "test"
)

libraryDependencies	<+= (scalaVersion) { "org.scala-lang" % "scala-reflect" % _ }

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
