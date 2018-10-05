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
package com.github.johnreedlol.implicits

final case class Pos(text: String)
object Pos {
  implicit def generate: Pos = macro implementation
  def implementation(c: scala.reflect.macros.blackbox.Context): c.Expr[Pos] = {
    import c.universe._
    val lineNum: String = c.enclosingPosition.line.toString
    val pathAndFileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
    val fileName: String = com.github.johnreedlol.getFileName(pathAndFileName)
    val path: String = c.internal.enclosingOwner.fullName.trim
    @SuppressWarnings(Array("org.wartremover.warts.Null"))
    val toReturn = q"""
        if(System.getenv("DISABLE_POS_DEBUG") == null) {
          "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")"
        } else {
          ""
        }
      """
    c.Expr[Pos](q"""com.github.johnreedlol.implicits.Pos.apply($toReturn);""")
  }
}