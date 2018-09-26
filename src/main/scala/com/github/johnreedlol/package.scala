package com.github

import com.github.johnreedlol.internal.Printer
import scala.language.experimental.macros

/**
  * Provides position based printing.
  * Code taken from https://github.com/JohnReedLOL/scala-trace-debug
  */
package object johnreedlol {

  /**
    * Prints the value along with a clickable hyperlink to the location in the source code to std out
    * @example out("Hello World")
    */
  object out {
    def apply(toPrint: Any): Unit = macro outImpl

    /**
      * Macro implementation.
      */
    def outImpl(c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Any]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val fileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName: String = processFileName(fileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val myStringTree: c.universe.Tree = toPrint.tree
      @SuppressWarnings(Array("org.wartremover.warts.Null"))
      val myString: c.universe.Tree = q"""{if($myStringTree == null) {"null"} else {$myStringTree.toString()}}""" // [wartremover:Null] null is disabled
      val toReturn = q"""
        _root_.scala.Console.println($myString + "\t" + "at " + $path + "(" + $trimmedFileName + ":" + $lineNum + ")");
      """
      c.Expr[Unit](toReturn)
    }
  }

  /**
    * Prints the value along with a clickable hyperlink to the location in the source code to std err
    * @example err("Hello World")
    */
  object err {
    def apply(toPrint: Any): Unit = macro errImpl

    /**
      * Macro implementation.
      */
    def errImpl(c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Any]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val fileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName: String = processFileName(fileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val myStringTree: c.universe.Tree = toPrint.tree
      @SuppressWarnings(Array("org.wartremover.warts.Null"))
      val myString: c.universe.Tree = q"""{if($myStringTree == null) {"null"} else {$myStringTree.toString()}}""" // [wartremover:Null] null is disabled
      val toReturn = q"""
        _root_.java.lang.System.err.println($myString + "\t" + "at " + $path + "(" + $trimmedFileName + ":" + $lineNum + ")");
      """
      c.Expr[Unit](toReturn)
    }
  }

  object pos {
    /**
      * Provides a file/line position as a string to append to the end of a print or log statement.
      * @example logger.warn("something has occurred" + pos()) // "something has occurred - path.to.MyClass.func(MyClass.scala:33)"
      */
    def apply(): String = macro posImpl

    /**
      * Do not call this - this is for internal use.
      */
    def posImpl(c: scala.reflect.macros.blackbox.Context)(): c.Expr[String] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val fileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName: String = processFileName(fileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val toReturn = q"""
        "\t" + "at " + $path + "(" + $trimmedFileName + ":" + $lineNum + ")"
      """
      c.Expr[String](toReturn)
    }
  }

  import com.github.johnreedlol.internal.Helpers.MacroHelperMethod // This is used to turn source code into String
  import scala.language.existentials // MacroHelperMethod uses the existential type feature

  /**
    * Prints the code in the block to standard err, not just the result
    * @example myVal = 3; codeErr{1 + 2 + myVal}
    */
  object codeErr {
    def traceCodeImpl(c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Any]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val fileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName: String = processFileName(fileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val blockString = new MacroHelperMethod[c.type](c).getSourceCode(toPrint.tree)
      @SuppressWarnings(Array("org.wartremover.warts.Any"))
      val myString = q""" "(" + $blockString + ") -> " + ({$toPrint}.toString) """ // [wartremover:Any] Inferred type containing Any
      // The java stack traces use a tab character \t, not a space.
      val toReturn = q"""
        _root_.java.lang.System.err.println($myString + "\t" + "at " + $path + "(" + $trimmedFileName + ":" + $lineNum + ")");
      """
      c.Expr[Unit](toReturn)
    }

    def apply(toPrint: Any): Unit = macro traceCodeImpl
  }

  /**
    * Prints the code in the block to standard out, not just the result
    * @example myVal = 3; codeOut{1 + 2 + myVal}
    */
  object codeOut {
    def traceCodeImpl(c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Any]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val fileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val trimmedFileName: String = processFileName(fileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val blockString = new MacroHelperMethod[c.type](c).getSourceCode(toPrint.tree)
      @SuppressWarnings(Array("org.wartremover.warts.Any"))
      val myString = q""" "(" + $blockString + ") -> " + ({$toPrint}.toString) """ // Inferred type containing Any
      // The java stack traces use a tab character \t, not a space.
      val toReturn = q"""
        _root_.scala.Console.println($myString + "\t" + "at " + $path + "(" + $trimmedFileName + ":" + $lineNum + ")");
      """
      c.Expr[Unit](toReturn)
    }

    def apply(toPrint: Any): Unit = macro traceCodeImpl
  }

  // Warning: You can't pass in : =>Boolean without getting "java.lang.IllegalArgumentException: Could not find proxy for val myVal"
  // You also cannot use default parameters. Boo.

  /**
    * Same as check, but prints the code instead of a user specified error message.
    * @example val one = 1; checkCode{one + 1 == 2}
    */
  object check {
    def apply(assertion: Boolean): Unit = macro checkCodeImpl

    def checkCodeImpl(c: scala.reflect.macros.blackbox.Context)(assertion: c.Expr[Boolean]): c.Expr[Unit] = {
      import c.universe._
      val sourceCode: c.Tree = new MacroHelperMethod[c.type](c).getSourceCode(assertion.tree)
      val arg2 = q""" "(" + $sourceCode + ") -> " + ({$assertion}.toString) """
      val args: List[c.universe.Tree] = List(arg2)
      /*
        def check(assertion: Boolean, message: String, numLines: Int = Int.MaxValue): String = {
        Printer.internalAssert(message, numLines, usingStdOut = false, assertionTrue_? = assertion)
      }
       */
      val toReturn = q"""
        val assertBoolean = $assertion;
        _root_.com.github.johnreedlol.check(assertBoolean, ..$args);
      """
      c.Expr[Unit](toReturn)
    }
  }

  /**
    * Like an assertion that does not terminate the current thread
    */
  def check(assertion: Boolean, message: String): Unit = {
    Printer.internalAssert(assertion, message)
  }

  protected[johnreedlol] def processFileName(fileName: String): String = {
    if (fileName.contains("/")) {
      fileName.split("/").last
    } else {
      fileName.split("\\\\").last
    }
  }

}
