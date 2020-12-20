import spray.boilerplate.BoilerplatePlugin
import sbtcrossproject.{ CrossProject, CrossType, Platform }

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.213.0",

	scalaVersion	:= "2.13.4",
	scalacOptions	++= Seq(
		"-feature",
		"-deprecation",
		"-unchecked",
		"-Werror",
		"-Xlint",
		"-language:implicitConversions",
	),
	conflictManager	:= ConflictManager.strict withOrganization "^(?!(org\\.scala-lang|org\\.scala-js)(\\..*)?)$",

	wartremoverErrors	++= Seq(
		Wart.AsInstanceOf,
		Wart.IsInstanceOf,
		Wart.StringPlusAny,
		Wart.ToString,
		Wart.EitherProjectionPartial,
		Wart.OptionPartial,
		Wart.TryPartial,
		Wart.Enumeration,
		Wart.FinalCaseClass,
		Wart.JavaConversions,
		Wart.Option2Iterable,
		Wart.JavaSerializable,
		//Wart.Any,
		Wart.AnyVal,
		//Wart.Nothing,
		Wart.ArrayEquals,
		Wart.ImplicitParameter,
		Wart.ExplicitImplicitTypes,
		Wart.LeakingSealed,
		Wart.DefaultArguments,
		Wart.Overloading,
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
		`scjson-io-converter`,
		`scjson-io-pickle`
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
			"de.djini"			%%%	"scutil-core"	% "0.192.0"				% "compile"
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
			"de.djini"			%%%	"scutil-core"	% "0.192.0"				% "compile",
			"io.monix"			%%	"minitest"		% "2.9.1"				% "test"
		)
	)
	.jvmSettings(
		testFrameworks	+= new TestFramework("minitest.runner.Framework"),
	)
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
			"de.djini"			%%%	"scutil-core"	% "0.192.0"				% "compile",
			"io.monix"			%%	"minitest"		% "2.9.1"				% "test"
		),
		// getParentFile because we are actually in .jvm or .js due to cross compilation
		Compile / boilerplateSource	:= baseDirectory.value.getParentFile / "src" / "main" / "boilerplate"
	)
	.jvmSettings(
		testFrameworks	+= new TestFramework("minitest.runner.Framework"),
	)
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
			"de.djini"			%%	"scutil-core"	% "0.192.0"				% "compile",
			"io.monix"			%%	"minitest"		% "2.9.1"				% "test"
		),
		testFrameworks	+= new TestFramework("minitest.runner.Framework"),
		Compile / boilerplateSource	:= baseDirectory.value / "src" / "main" / "boilerplate"
	)

lazy val `scjson-io-pickle`	=
	(project in file("modules/io-pickle"))
	.enablePlugins()
	.dependsOn(
		`scjson-ast-jvm`,
		`scjson-codec-jvm`,
		`scjson-pickle`
	)
	.settings(
		libraryDependencies	++= Seq(
			"de.djini"			%%	"scutil-jdk"	% "0.192.0"				% "compile"
		)
	)

lazy val `scjson-io-converter`	=
	(project in file("modules/io-converter"))
	.enablePlugins()
	.dependsOn(
		`scjson-ast-jvm`,
		`scjson-codec-jvm`,
		`scjson-converter-jvm`
	)
	.settings(
		libraryDependencies	++= Seq(
			"de.djini"			%%	"scutil-jdk"	% "0.192.0"				% "compile"
		)
	)
