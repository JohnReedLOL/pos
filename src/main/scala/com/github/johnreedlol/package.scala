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
    def apply[Type](toPrint: Type): Unit = macro outImpl[Type]

    /**
      * Macro implementation.
      */
    def outImpl[Type](c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Type]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      @SuppressWarnings(Array("org.wartremover.warts.Null", "org.wartremover.warts.Nothing"))
      val myString: c.universe.Tree = q"""{if($toPrint == null) {"null"} else {$toPrint.toString()}}""" // [wartremover:Null] null is disabled
      val toReturn = q"""
        _root_.scala.Console.println($myString + "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")");
      """
      c.Expr[Unit](toReturn)
    }
  }

  /**
    * Prints the value along with a clickable hyperlink to the location in the source code to std err
    * @example err("Hello World")
    */
  object err {
    def apply[Type](toPrint: Type): Unit = macro errImpl[Type]

    /**
      * Macro implementation.
      */
    def errImpl[Type](c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Type]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      @SuppressWarnings(Array("org.wartremover.warts.Null", "org.wartremover.warts.Nothing"))
      val myString: c.universe.Tree = q"""{if($toPrint == null) {"null"} else {$toPrint.toString()}}""" // [wartremover:Null] null is disabled
      val toReturn = q"""
        _root_.java.lang.System.err.println($myString + "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")");
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
      val pathAndFileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val toReturn = q"""
        "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")"
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
    def apply[Type](toPrint: Type): Unit = macro traceCodeImpl[Type]

    /**
      * Macro implementation.
      */
    def traceCodeImpl[Type](c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Type]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val blockString = new MacroHelperMethod[c.type](c).getSourceCode(toPrint.tree)
      @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Null"))
      val myString: c.universe.Tree = q"""{if($toPrint == null) {"null"} else {"(" + $blockString + ") -> " + ({$toPrint}.toString)}}"""
      // The java stack traces use a tab character \t, not a space.
      val toReturn = q"""
        _root_.java.lang.System.err.println($myString + "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")");
      """
      c.Expr[Unit](toReturn)
    }
  }

  /**
    * Prints the code in the block to standard out, not just the result
    * @example myVal = 3; codeOut{1 + 2 + myVal}
    */
  object codeOut {
    def apply[Type](toPrint: Type): Unit = macro traceCodeImpl[Type]

    /**
      * Macro implementation.
      */
    def traceCodeImpl[Type](c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Type]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val blockString = new MacroHelperMethod[c.type](c).getSourceCode(toPrint.tree)
      @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Null"))
      val myString: c.universe.Tree = q"""{if($toPrint == null) {"null"} else {"(" + $blockString + ") -> " + ({$toPrint}.toString)}}"""
      // The java stack traces use a tab character \t, not a space.
      val toReturn = q"""
        _root_.scala.Console.println($myString + "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")");
      """
      c.Expr[Unit](toReturn)
    }
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
      val toReturn = q"""
        val assertBoolean = $assertion;
        _root_.com.github.johnreedlol.checkWithMessage(assertBoolean, ..$args);
      """
      c.Expr[Unit](toReturn)
    }
  }

  /**
    * Like an assertion that does not terminate the current thread.
    */
  def checkWithMessage(assertion: Boolean, message: String): Unit = {
    Printer.internalAssert(assertion, message)
  }

  protected[johnreedlol] def getFileName(path: String): String = {
    if (path.contains("/")) {
      path.split("/").last
    } else {
      path.split("\\\\").last
    }
  }

}
