name := "pos"

organization := "com.github.johnreedlol"

description := "Macro based print debugging. Locates log statements in your IDE."

developers := List(Developer(id = "johnreedlol", name = "John-Michael Reed", email = "johnmichaelreedfas@gmail.com", new URL("https://github.com/JohnReedLOL")))

scmInfo := Some(ScmInfo(new URL("https://github.com/JohnReedLOL/scala-trace-debug"),
  "scm:git:git://github.com/JohnReedLOL/scala-trace-debug.git",
  None))

pomExtra := <url>https://github.com/JohnReedLOL/scala-trace-debug</url>

scalaVersion := "2.12.6"

version := "1.0.0" // For compatibility, only use first two digits (MajorVersion, MinorVersion)

crossScalaVersions := Seq("2.11.12", "2.12.6", "2.13.0-M4")

resolvers += Resolver.sonatypeRepo("releases")

pgpReadOnly := false //  To import a key

useGpg := true // The first step towards using the GPG command line tool is to make sbt-pgp gpg-aware. (skip for built-in Bouncy Castle PGP implementation)

def macroDependencies(version: String): Seq[ModuleID] = Seq(
  "org.scala-lang" % "scala-reflect" % version % "provided",
  "org.scala-lang" % "scala-compiler" % version % "provided"
)

libraryDependencies ++= macroDependencies(scalaVersion.value)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xlint", "-Ywarn-inaccessible",
  "-Ywarn-nullary-override", "-Ywarn-nullary-unit", "-Xfatal-warnings")

bintrayReleaseOnPublish in ThisBuild := true

bintrayOmitLicense := false

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayPackageLabels := Seq("debug", "scala", "trace", "debugging", "assert", "pos")

bintrayVcsUrl := Some("git@github.com:JohnReedLOL/scala-trace-debug.git")

// Turn on all checks that are currently considered stable:
wartremoverErrors ++= Warts.unsafe