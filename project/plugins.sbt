// Copyright (c) 2016 John-Michael Reed.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the MIT License;
// You may obtain a copy of the License at:
//
// https://opensource.org/licenses/MIT
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.4")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

// Removes warts like null
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.3.7")

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