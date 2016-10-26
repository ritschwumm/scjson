inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.100.0",
	
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
		"-Xfatal-warnings",
		"-Xlint"
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
			`scjson-ast-jvm`,
			`scjson-ast-js`,
			`scjson-codec-jvm`,
			`scjson-codec-js`,
			`scjson-pickle`,
			`scjson-io`
		)
		.settings(
			publishArtifact := false
			//publish		:= {},
			//publishLocal	:= {}
		)
	
lazy val `scjson-ast`	=
		(crossProject crossType CrossType.Pure	in	file("sub/ast"))
		.enablePlugins()
		.settings(
			libraryDependencies	++= Seq(
				"de.djini"			%%%	"scutil-base"	% "0.91.0"				% "compile"
			),
			wartremoverErrors	++= warts
		)
		.jvmSettings()
		.jsSettings()
lazy val `scjson-ast-jvm`	= `scjson-ast`.jvm
lazy val `scjson-ast-js`	= `scjson-ast`.js

lazy val `scjson-codec`	=
		(crossProject crossType CrossType.Pure	in	file("sub/codec"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast`
		)
		.settings(
			libraryDependencies	++= Seq(
				"de.djini"			%%%	"scutil-base"	% "0.91.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "3.8.5"				% "test"
			),
			wartremoverErrors	++= warts
		)
		.jvmSettings()
		.jsSettings()
lazy val `scjson-codec-jvm`	= `scjson-codec`.jvm
lazy val `scjson-codec-js`	= `scjson-codec`.js

lazy val `scjson-pickle`	=
		(project in file("sub/pickle"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast-jvm`
		)
		.settings(
			libraryDependencies	++= Seq(      
				"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
				"de.djini"			%%	"scutil-base"	% "0.91.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "3.8.5"				% "test"
			),
			wartremoverErrors	++= warts,
			(sourceGenerators in Compile)	+=
					(Def.task {
						Boilerplate generate (sourceManaged in Compile).value
					}).taskValue
		)
		
lazy val `scjson-io`	=
		(project in file("sub/io"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast-jvm`,
			`scjson-codec-jvm`,
			`scjson-pickle`
		)
		.settings(
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-core"	% "0.91.0"				% "compile"
			),
			wartremoverErrors	++= warts
		)
		