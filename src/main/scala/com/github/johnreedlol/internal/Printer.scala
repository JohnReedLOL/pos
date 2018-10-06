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
  * Created by johnreed on 4/12/16 for https://github.com/JohnReedLOL/scala-trace-debug
  */
protected[johnreedlol] object Printer {

  /**
    * Stack offset is 2 because the first row in the stack trace is Thread and the second row is internal call
    */
  protected[johnreedlol] val stackOffset = 2

  /** The offset of the first line from the base of the stack trace
    * The +1 is necessary because the method call internalAssert adds one to the offset of the stack trace
    */
  protected[johnreedlol] val newStackOffset: Int = stackOffset + 1

  /**
    * Gets the package name
    */
  protected[internal] def getPackageName(stackLine: StackTraceElement): String = {
    try {
      val className: Class[_] = Class.forName(stackLine.getClassName)
      val stringLocation: String = if (className != null) {
        val packageName: String = PackagingDataCalculator.getCodeLocation(className)
        if (packageName.endsWith(".jar")) {
          packageName
        } else {
          ""
        }
      } else {
        ""
      }
      stringLocation
    } catch {
      case _: java.lang.Exception => ""
    }
  }

  /** Prints out the object with N lines of stack trace. Meant to be used only for asserts
    *
    * @param toPrintOutNullable    the object to print out. May be "null"
    */
  protected[johnreedlol] final def internalAssert(
     assertionTrue_? : Boolean, toPrintOutNullable: String): Unit = {
    if (!assertionTrue_?) {
      val toPrintOut: String = if (toPrintOutNullable == null) {
        "null"
      } else {
        toPrintOutNullable
      }
      @SuppressWarnings(Array("org.wartremover.warts.Var"))
      var toPrint: String = "\"" + toPrintOut + "\"" + " in thread " + Thread.currentThread().getName + ":" // [wartremover:Var] var is disabled
      // Only make call to Thread.currentThread().getStackTrace if there is a stack to print
      val stack: Array[StackTraceElement] = Thread.currentThread().getStackTrace
      for (row <- 0 to stack.length - 1 - newStackOffset) {
        val lineNumber: Int = newStackOffset + row
        val stackLine: StackTraceElement = stack(lineNumber)
        val packageName: String = getPackageName(stackLine)
        val myPackageName: String = if (packageName.equals("")) {
          ""
        } else {
          " [" + packageName + "]"
        }
        // The java stack traces use a tab character, not a space
        val tab = "\t"
        toPrint += "\n" + tab + "at " + stackLine.toString + myPackageName
      }
      toPrint += "\n" + "^ The above stack trace leads to an assertion failure. ^"
      PrintLocker.synchronized {
        System.err.println(toPrint)
      }
    }
  }

  /**
    * Ensures that no two threads can print at the same time
    */
  private object PrintLocker

}
