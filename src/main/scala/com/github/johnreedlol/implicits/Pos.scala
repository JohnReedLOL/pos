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
