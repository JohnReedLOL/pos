/*
* Copyright 2018 John Michael Reed
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
name := "pos"

organization := "com.github.johnreedlol"

description := "Macro based print debugging. Locates log statements in your IDE."

developers := List(Developer(id = "johnreedlol", name = "John-Michael Reed", email = "johnmichaelreedfas@gmail.com", new URL("https://github.com/JohnReedLOL")))

scmInfo := Some(ScmInfo(new URL("https://github.com/JohnReedLOL/pos"),
  "scm:git:git://github.com/JohnReedLOL/pos.git",
  None))

pomExtra := <url>https://github.com/JohnReedLOL/pos</url>

scalaVersion := "2.12.8"

version := "2.2.0" // For compatibility, only use first two digits (MajorVersion, MinorVersion)

crossScalaVersions := Seq("2.11.12", "2.12.8", "2.13.0-M5")

resolvers += Resolver.sonatypeRepo("releases")

pgpReadOnly := false //  To import a key

useGpg := true // The first step towards using the GPG command line tool is to make sbt-pgp gpg-aware. (skip for built-in Bouncy Castle PGP implementation)

def macroDependencies(version: String): Seq[ModuleID] = Seq(
  "org.scala-lang" % "scala-reflect" % version % "provided",
  "org.scala-lang" % "scala-compiler" % version % "provided"
)

libraryDependencies ++= macroDependencies(scalaVersion.value)

// A compatible logging backend is Logback, add it to your sbt build definition:
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

scalacOptions ++= Seq("-Xlint")

// Taken from https://tpolecat.github.io/2017/04/25/scalac-flags.html

scalacOptions ++= Seq(
  "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8",                // Specify character encoding used by source files.
  "-explaintypes",                     // Explain type errors in more detail.
  "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros",     // Allow macro definition (besides implementation and application)
  "-language:higherKinds",             // Allow higher-kinded types
  "-language:implicitConversions",     // Allow definition of implicit functions called views
  "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
  "-Ywarn-dead-code"                  // Warn when dead code is identified.
)

skip in publish := false

bintrayReleaseOnPublish in ThisBuild := true

bintrayOmitLicense := false

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

bintrayPackageLabels := Seq("debug", "scala", "trace", "debugging", "assert", "pos")

bintrayVcsUrl := Some("git@github.com:JohnReedLOL/pos.git")

// Turn on all warts except for equals. We ignore this wart because we don't have cats/scalaz === method.
wartremoverErrors ++= Warts.allBut(Wart.Equals)