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

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.5")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.2")

// Removes warts like null
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.1")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.4.0")

// resolvers += Resolver.sonatypeRepo("releases")

// addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

/** Error during compilation of source code that depends on macros that access the source code:
  *
  * [info] Setting version to 2.10.4
  * exception during macro expansion:
  * [error] java.lang.UnsupportedOperationException: Position.start on class scala.reflect.internal.util.OffsetPosition
  * [error]         at scala.reflect.internal.util.Position.start(Position.scala:114)
  *
  * As a workaround, you can pass -Dsbt.parser.simple=true to your play/SBT command line. This will revert to the old .sbt parser that splits based on \n\n.
  */