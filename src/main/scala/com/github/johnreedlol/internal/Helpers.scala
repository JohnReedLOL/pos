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
package com.github.johnreedlol.internal

/**
  * Created by johnreed on 4/17/16. A place for helper methods.
  */
protected[johnreedlol] object Helpers {

  final class MacroHelperMethod[C <: scala.reflect.macros.blackbox.Context](val c: C) {

    @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
    def getSourceCode(toPrint: c.Tree): c.Tree = {
      import c.universe._
      val fileContent: String = new String(toPrint.pos.source.content)
      // apply case tree => tree.pos.startOrPoint to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = toPrint.collect {
        case treeVal => treeVal.pos match {
          case NoPosition ⇒ Int.MaxValue
          // The start of the position's range, or point if not a range position.
          case p ⇒ p.start
        }
      }
      @SuppressWarnings(Array("org.wartremover.warts.TraversableOps"))
      val start: Int = listOfTreePositions.min // ignore [wartremover:TraversableOps] min is disabled - use foldLeft or foldRight instead
      import scala.language.existentials
      @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
      val globalContext = c.asInstanceOf[reflect.macros.runtime.Context].global // inferred existential
      val codeParser: globalContext.syntaxAnalyzer.UnitParser = globalContext.newUnitParser(code = fileContent.drop(start))
      codeParser.expr() // This returns a value which is being ignored [wartremover:NonUnitStatements].
      val end: globalContext.syntaxAnalyzer.Offset = codeParser.in.lastOffset
      val text: String = fileContent.slice(start, start + end)
      val sourceCode = q""" $text """
      sourceCode
    }
  }

}
