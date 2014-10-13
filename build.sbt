name			:= "scjson"

organization	:= "de.djini"

version			:= "0.57.0"

scalaVersion	:= "2.11.2"

resolvers		+= "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies	++= Seq(
	"de.djini"			%%	"scutil-core"	% "0.52.0"				% "compile",
	"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
	"org.specs2"		%%	"specs2"		% "2.4.6"				% "test"
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
