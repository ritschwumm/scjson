inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.94.0",
	
	scalaVersion	:= "2.11.8",
	scalacOptions	++= Seq(
		"-deprecation",
		"-unchecked",
		"-language:implicitConversions",
		"-language:existentials",
		// "-language:higherKinds",
		// "-language:reflectiveCalls",
		// "-language:dynamics",
		// "-language:postfixOps",
		// "-language:experimental.macros"
		"-feature",
		"-Ywarn-unused-import",
		"-Xfatal-warnings"
	),
	conflictManager	:= ConflictManager.strict,
	resolvers		+= "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
))

lazy val warts	=
		Seq(
			Wart.Any2StringAdd,
			Wart.EitherProjectionPartial,
			Wart.OptionPartial,
			Wart.Enumeration,
			Wart.FinalCaseClass,
			Wart.JavaConversions,
			Wart.Option2Iterable,
			Wart.TryPartial
		)

lazy val `scjson` =
	(project in file("."))
	.aggregate(
		`scjson-ast`,
		`scjson-codec`,
		`scjson-pickle`,
		`scjson-io`
	)
	.settings(
		publishArtifact := false
		//publish		:= {},
		//publishLocal	:= {}
	)
	
lazy val `scjson-ast`	=
		(project in file("sub/ast"))
		.enablePlugins()
		.settings(
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-base"	% "0.87.0"				% "compile"
			),
			wartremoverErrors	++= warts
		)

lazy val `scjson-codec`	=
		(project in file("sub/codec"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast`
		)
		.settings(
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-base"	% "0.87.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "3.8.4"				% "test"
			),
			wartremoverErrors	++= warts
		)
		
lazy val `scjson-pickle`	=
		(project in file("sub/pickle"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast`
		)
		.settings(
			libraryDependencies	++= Seq(      
				"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
				"de.djini"			%%	"scutil-base"	% "0.87.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "3.8.4"				% "test"
			),
			wartremoverErrors	++= warts,
			(sourceGenerators in Compile)	<+= (sourceManaged in Compile) map Boilerplate.generate
		)
		
lazy val `scjson-io`	=
		(project in file("sub/io"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast`,
			`scjson-codec`,
			`scjson-pickle`
		)
		.settings(
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-core"	% "0.87.0"				% "compile"
			),
			wartremoverErrors	++= warts
		)
		