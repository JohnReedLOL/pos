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
    @SuppressWarnings(Array("org.wartremover.warts.Null"))
    def outImpl[Type](c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Type]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      @SuppressWarnings(Array("org.wartremover.warts.Nothing"))
      val myString: c.universe.Tree = q"""{if($toPrint == null) {"null"} else {$toPrint.toString()}}""" // [wartremover:Null] null is disabled
      val toReturn = q"""
        if(System.getenv("DISABLE_POS_DEBUG") == null) {
          _root_.scala.Console.println($myString + "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")");
        };
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
    @SuppressWarnings(Array("org.wartremover.warts.Null"))
    def errImpl[Type](c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Type]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      @SuppressWarnings(Array("org.wartremover.warts.Nothing"))
      val myString: c.universe.Tree = q"""{if($toPrint == null) {"null"} else {$toPrint.toString()}}""" // [wartremover:Null] null is disabled
      val toReturn = q"""
        if(System.getenv("DISABLE_POS_DEBUG") == null) {
          _root_.java.lang.System.err.println($myString + "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")");
        };
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
    @SuppressWarnings(Array("org.wartremover.warts.Null"))
    def posImpl(c: scala.reflect.macros.blackbox.Context)(): c.Expr[String] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path // This needs to be trimmed down
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val toReturn = q"""
        if(System.getenv("DISABLE_POS_DEBUG") == null) {
          "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")"
        } else {
          ""
        };
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
    @SuppressWarnings(Array("org.wartremover.warts.Null"))
    def traceCodeImpl[Type](c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Type]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val blockString = new MacroHelperMethod[c.type](c).getSourceCode(toPrint.tree)
      @SuppressWarnings(Array("org.wartremover.warts.Any"))
      val myString: c.universe.Tree = q"""{if($toPrint == null) {"null"} else {"(" + $blockString + ") -> " + ({$toPrint}.toString)}}"""
      // The java stack traces use a tab character \t, not a space.
      val toReturn = q"""
        if(System.getenv("DISABLE_POS_DEBUG") == null) {
          _root_.java.lang.System.err.println($myString + "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")");
        };
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
    @SuppressWarnings(Array("org.wartremover.warts.Null"))
    def traceCodeImpl[Type](c: scala.reflect.macros.blackbox.Context)(toPrint: c.Expr[Type]): c.Expr[Unit] = {
      import c.universe._
      val lineNum: String = c.enclosingPosition.line.toString
      val pathAndFileName: String = c.enclosingPosition.source.path
      val fileName: String = getFileName(pathAndFileName)
      val path: String = c.internal.enclosingOwner.fullName.trim
      val blockString = new MacroHelperMethod[c.type](c).getSourceCode(toPrint.tree)
      @SuppressWarnings(Array("org.wartremover.warts.Any"))
      val myString: c.universe.Tree = q"""{if($toPrint == null) {"null"} else {"(" + $blockString + ") -> " + ({$toPrint}.toString)}}"""
      // The java stack traces use a tab character \t, not a space.
      val toReturn = q"""
        if(System.getenv("DISABLE_POS_DEBUG") == null) {
          _root_.scala.Console.println($myString + "\t" + "at " + $path + "(" + $fileName + ":" + $lineNum + ")");
        };
      """
      c.Expr[Unit](toReturn)
    }
  }

  /**
    * Like an assertion that does not terminate the current thread.
    * Prints the assertion as a String.
    */
  object check {
    def apply(assertion: Boolean): Unit = macro checkCodeImpl

    def checkCodeImpl(c: scala.reflect.macros.blackbox.Context)(assertion: c.Expr[Boolean]): c.Expr[Unit] = {
      import c.universe._
      val sourceCode: c.Tree = new MacroHelperMethod[c.type](c).getSourceCode(assertion.tree)
      val message = q""" "(" + $sourceCode + ") -> " + ({$assertion}.toString) """
      val toReturn = q"""
        _root_.com.github.johnreedlol.checkWithMessage($assertion, $message);
      """
      c.Expr[Unit](toReturn)
    }
  }

  /**
    * Like an assertion that does not terminate the current thread.
    */
  def checkWithMessage(assertion: Boolean, message: String): Unit = {
    if(System.getenv("DISABLE_POS_DEBUG") == null) {
      Printer.internalAssert(assertion, message)
    }
  }

  protected[johnreedlol] def getFileName(path: String): String = {
    if (path.contains("/")) {
      path.split("/").last
    } else {
      path.split("\\\\").last
    }
  }

}
