import org.scalajs.sbtplugin.cross.CrossProject
import spray.boilerplate.BoilerplatePlugin

inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.148.0",
	
	scalaVersion	:= "2.12.4",
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
			Wart.TryPartial,
			Wart.JavaSerializable,
			//Wart.Any,
			Wart.AnyVal,
			//Wart.Nothing,
			Wart.ArrayEquals,
			Wart.ExplicitImplicitTypes,
			Wart.LeakingSealed
			//Wart.Overloading
			//Wart.PublicInference,
			//Wart.TraversableOps
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
		myCrossProject("scjson-ast", file("modules/ast"))
		.enablePlugins()
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(
				"de.djini"			%%%	"scutil-base"	% "0.135.0"				% "compile"
			)
		)
		.jvmSettings()
		.jsSettings(
			noTestSettings
		)
lazy val `scjson-ast-jvm`	= `scjson-ast`.jvm
lazy val `scjson-ast-js`	= `scjson-ast`.js

lazy val `scjson-codec`	=
		myCrossProject("scjson-codec", file("modules/codec"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast`
		)
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(
				"de.djini"			%%%	"scutil-base"	% "0.135.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "4.0.3"				% "test"
			)
		)
		.jvmSettings()
		.jsSettings(
			noTestSettings
		)
lazy val `scjson-codec-jvm`	= `scjson-codec`.jvm
lazy val `scjson-codec-js`	= `scjson-codec`.js

lazy val `scjson-pickle`	=
		(project in file("modules/pickle"))
		.enablePlugins(
			BoilerplatePlugin
		)
		.dependsOn(
			`scjson-ast-jvm`
		)
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(      
				"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
				"de.djini"			%%	"scutil-base"	% "0.135.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "4.0.3"				% "test"
			),
			boilerplateSource in Compile := baseDirectory.value / "src" / "main" / "boilerplate"
		)
		
lazy val `scjson-io`	=
		(project in file("modules/io"))
		.enablePlugins()
		.dependsOn(
			`scjson-ast-jvm`,
			`scjson-codec-jvm`,
			`scjson-pickle`
		)
		.settings(
			wartRemoverSetting,
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-core"	% "0.135.0"				% "compile"
			)
		)
		