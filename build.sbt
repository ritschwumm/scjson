import spray.boilerplate.BoilerplatePlugin
import sbtcrossproject.{ CrossProject, CrossType, Platform }

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(Seq(
	organization	:= "de.djini",
	version			:= "0.240.0",

	scalaVersion	:= "3.1.0",
	scalacOptions	++= Seq(
		"-feature",
		"-deprecation",
		"-unchecked",
		"-Wunused:all",
		"-Xfatal-warnings",
		"-Ykind-projector:underscores",
	),

	versionScheme	:= Some("early-semver"),
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
		`scjson-io`,
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
			"de.djini"			%%%	"scutil-core"	% "0.214.0"				% "compile"
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
			"de.djini"			%%%	"scutil-core"	% "0.214.0"				% "compile",
			"io.monix"			%%	"minitest"		% "2.9.6"				% "test"
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
			"de.djini"			%%%	"scutil-core"	% "0.214.0"				% "compile",
			"io.monix"			%%	"minitest"		% "2.9.6"				% "test"
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

lazy val `scjson-io`	=
	(project in file("modules/io"))
	.enablePlugins()
	.dependsOn(
		`scjson-ast-jvm`,
		`scjson-codec-jvm`,
		`scjson-converter-jvm`
	)
	.settings(
		libraryDependencies	++= Seq(
			"de.djini"			%%	"scutil-jdk"	% "0.214.0"				% "compile"
		)
	)
