name			:= "scjson"

organization	:= "de.djini"

version			:= "0.0.5"

scalaVersion	:= "2.9.1"

//publishArtifact in (Compile, packageBin)	:= false

publishArtifact in (Compile, packageDoc)	:= false

publishArtifact in (Compile, packageSrc)	:= false

libraryDependencies	++= Seq(
	"de.djini"		%%	"scutil"	% "0.0.5"	% "compile",
	"de.djini"		%%	"scmirror"	% "0.0.1"	% "compile",
	"org.specs2"	%% "specs2"		% "1.8.1"	% "test"
)

scalacOptions	++= Seq("-deprecation", "-unchecked")

(sourceGenerators in Compile)	<+= (sourceManaged in Compile) map Boilerplate.generate
