import org.scalajs.sbtplugin.cross.CrossProject

inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.119.0",
	
	scalaVersion	:= "2.12.2",
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
		"-Xfatal-warnings",
		"-Xlint"
	),
	conflictManager	:= ConflictManager.strict,
	resolvers		+= "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
))

lazy val noTestSettings	=
		Seq(
			test		:= {},
			testQuick	:= {}
		)
		
lazy val wartRemoverSetting	=
		wartremoverErrors	++= Seq(
			Wart.StringPlusAny,
			Wart.EitherProjectionPartial,
			Wart.OptionPartial,
			Wart.Enumeration,
			Wart.FinalCaseClass,
			Wart.JavaConversions,
			Wart.Option2Iterable,
			Wart.TryPartial
		)
		
// (crossProject crossType CrossType.Pure in base)
def myCrossProject(id:String, base:File):CrossProject	=
		CrossProject(id + "-jvm", id + "-js", base, CrossType.Pure).settings(name := id)

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
	
//------------------------------------------------------------------------------
		
lazy val `scjson-ast`	=
		myCrossProject("scjson-ast", file("sub/ast"))
		.enablePlugins()
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(
				"de.djini"			%%%	"scutil-base"	% "0.108.0"				% "compile"
			)
		)
		.jvmSettings()
		.jsSettings(
			noTestSettings
		)
lazy val `scjson-ast-jvm`	= `scjson-ast`.jvm
lazy val `scjson-ast-js`	= `scjson-ast`.js

lazy val `scjson-codec`	=
		myCrossProject("scjson-codec", file("sub/codec"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast`
		)
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(
				"de.djini"			%%%	"scutil-base"	% "0.108.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "3.8.9"				% "test"
			)
		)
		.jvmSettings()
		.jsSettings(
			noTestSettings
		)
lazy val `scjson-codec-jvm`	= `scjson-codec`.jvm
lazy val `scjson-codec-js`	= `scjson-codec`.js

lazy val `scjson-pickle`	=
		(project in file("sub/pickle"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast-jvm`
		)
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(      
				"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
				"de.djini"			%%	"scutil-base"	% "0.108.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "3.8.9"				% "test"
			),
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
			wartRemoverSetting,
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-core"	% "0.108.0"				% "compile"
			)
		)
		