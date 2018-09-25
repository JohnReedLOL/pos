package com.github.johnreedlol.internal

/**
  * Created by johnreed on 4/17/16. A place for helper methods.
  */
protected[johnreedlol] object Helpers {

  final class MacroHelperMethod[C <: scala.reflect.macros.blackbox.Context](val c: C) {

    @SuppressWarnings(Array("org.wartremover.warts.TraversableOps", "org.wartremover.warts.NonUnitStatements", "org.wartremover.warts.AsInstanceOf"))
    def getSourceCode(toPrint: c.Tree): c.Tree = {
      import c.universe._
      val fileContent: String = new String(toPrint.pos.source.content)
      // apply case tree => tree.pos.startOrPoint to each subtree on which the function is defined and collect the results.
      val listOfTreePositions: List[Int] = toPrint.collect {
        case treeVal => treeVal.pos match {
          case NoPosition ⇒ Int.MaxValue
          /** The start of the position's range, or point if not a range position. */
          case p ⇒ p.start
        }
      }
      val start: Int = listOfTreePositions.min // ignore [wartremover:TraversableOps] min is disabled - use foldLeft or foldRight instead
      import scala.language.existentials
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
