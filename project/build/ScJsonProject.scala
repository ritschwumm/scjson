import sbt._

final class ScJsonProject(info:ProjectInfo) extends DefaultProject(info) {
	// dependencies
	val scutil 	= "de.djini"				%% "scutil"	% "0.0.1"	% "compile"
	val specs	= "org.scala-tools.testing"	%% "specs"	% "1.6.7.1"	% "test"

	// issue compiler warnings
	override def compileOptions	= super.compileOptions ++ Seq(Unchecked)
	
	// publish sources
	override def packageSrcJar	= defaultJarPath("-sources.jar")
	val sourceArtifact	= Artifact.sources(artifactID)
	override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)
	
	/*
	override def packageDocsJar	= defaultJarPath("-javadoc.jar")
	override def packageSrcJar	= defaultJarPath("-sources.jar")
	val sourceArtifact	= Artifact.sources(artifactID)
	val docsArtifact	= Artifact.javadoc(artifactID)
	override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageDocs, packageSrc)
	*/
}
