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
