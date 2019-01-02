import spray.boilerplate.BoilerplatePlugin
import sbtcrossproject.{ CrossProject, CrossType, Platform }

inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.163.0",

	scalaVersion	:= "2.12.7",
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
	resolvers		+= "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",

	wartremoverErrors	++= Seq(
		Wart.AsInstanceOf,
		Wart.IsInstanceOf,
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
		Wart.LeakingSealed,
		//Wart.Overloading
		//Wart.PublicInference,
		Wart.TraversableOps
	)
))

lazy val noTestSettings	=
		Seq(
			test		:= {},
			testQuick	:= {}
		)

// (crossProject crossType CrossType.Pure in base)
def myCrossProject(id:String, base:File):CrossProject	=
		CrossProject(
			id		= id,
			base	= base,
		)(
			JVMPlatform,
			JSPlatform
		)
		.crossType(CrossType.Pure)
		.settings(
			name := id
		)
		.configurePlatform(JVMPlatform)	(_ withId (id + "-jvm"))
		.configurePlatform(JSPlatform)	(_ withId (id + "-js"))

lazy val `scjson` =
		(project in file("."))
		.aggregate(
			`scjson-ast-jvm`,
			`scjson-ast-js`,
			`scjson-codec-jvm`,
			`scjson-codec-js`,
			`scjson-converter-jvm`,
			`scjson-converter-js`,
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
			libraryDependencies	++= Seq(
				"de.djini"			%%%	"scutil-base"	% "0.147.0"				% "compile"
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
			libraryDependencies	++= Seq(
				"de.djini"			%%%	"scutil-base"	% "0.147.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "4.3.5"				% "test"
			)
		)
		.jvmSettings()
		.jsSettings(
			noTestSettings
		)
lazy val `scjson-codec-jvm`	= `scjson-codec`.jvm
lazy val `scjson-codec-js`	= `scjson-codec`.js

lazy val `scjson-converter`	=
		myCrossProject("scjson-converter", file("modules/converter"))
		.enablePlugins(
			BoilerplatePlugin
		)
		.dependsOn(
			`scjson-ast`,
			`scjson-codec`
		)
		.settings(
			libraryDependencies	++= Seq(
				//"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "provided",
				"de.djini"			%%%	"scutil-base"	% "0.147.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "4.3.5"				% "test"
			),
			// getParentFile because we are actually in .jvm or .js due to cross compilation
			boilerplateSource in Compile := baseDirectory.value.getParentFile / "src" / "main" / "boilerplate"
		)
		.jvmSettings()
		.jsSettings(
			noTestSettings
		)
lazy val `scjson-converter-jvm`	= `scjson-converter`.jvm
lazy val `scjson-converter-js`	= `scjson-converter`.js

lazy val `scjson-pickle`	=
		(project in file("modules/pickle"))
		.enablePlugins(
			BoilerplatePlugin
		)
		.dependsOn(
			`scjson-ast-jvm`
		)
		.settings(
			libraryDependencies	++= Seq(
				// TODO could this be a provided dependency?
				// TODO is this dependency necessary at all?
				"org.scala-lang"	%	"scala-reflect"	% scalaVersion.value	% "compile",
				"de.djini"			%%	"scutil-base"	% "0.147.0"				% "compile",
				"org.specs2"		%%	"specs2-core"	% "4.3.5"				% "test"
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
			libraryDependencies	++= Seq(
				"de.djini"			%%	"scutil-core"	% "0.147.0"				% "compile"
			)
		)

