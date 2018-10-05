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
package my.pkg

/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {

  /**
    * To prevent output mangling
    */
  def sleep(): Unit = {
    val milliseconds = 60L
    Thread.sleep(milliseconds)
  }

  def main(args: Array[String]): Unit = {
    /*
     * Note: `out(5)` emits "comparing values of types Int and Null using `==' will always yield false"
     * This was fixed in version 1.2.0, but that fix required method overloading and caused problems when passing in values of type "Any"
     * So I removed that fix in version 1.3.0
     */
    import com.github.johnreedlol._
    out(5)
    out("Hello")
    sleep()
    err("World")
    err(6)
    sleep()
    println("This will contain a compiler generated stack trace" + pos())
    println("This line will not contain a compiler generated stack trace.")
    val one = 1
    val two = 2
    codeOut(one + two)
    sleep()
    val three = 3
    val four = 4
    codeErr(three * four)
    check(three != four)
    checkWithMessage(three != four, "Three must not equal four")
    // This should generate a stack trace unless "DISABLE_POS_DEBUG" is set.
    check(three == four)
    checkWithMessage(three == four, "Three must not equal four")
    sleep()
    runNullSafetyTest()
  }

  /**
    * pos must not throw a null pointer even if null is passed in.
    */
  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def runNullSafetyTest(): Unit = {
    import com.github.johnreedlol._
    val nullVal: String = null
    out(nullVal)
    sleep()
    err(nullVal)
    sleep()
    out.apply(nullVal)
    sleep()
    err.apply(nullVal)
    sleep()
    println(nullVal)
    sleep()
    codeOut.apply(nullVal)
    sleep()
    codeErr.apply(nullVal)
    sleep()
  }
}

/*
Output if environment variable DISABLE_POS_DEBUG is not set:
username$ sbt test:run
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/username/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/username/Downloads/scala-trace-debug/5.0/project
[info] Loading settings from build.sbt ...
[info] Set current project to pos (in build file:/Users/username/Downloads/scala-trace-debug/5.0/)
[info] Compiling 1 Scala source to /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/test-classes ...
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:49:8: comparing values of types Int and Null using `==' will always yield false
[warn]     out(5)
[warn]        ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:53:8: comparing values of types Int and Null using `==' will always yield false
[warn]     err(6)
[warn]        ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:59:12: comparing values of types Int and Null using `==' will always yield false
[warn]     codeOut(one + two)
[warn]            ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:63:12: comparing values of types Int and Null using `==' will always yield false
[warn]     codeErr(three * four)
[warn]            ^
[warn] four warnings found
[info] Done compiling.
[info] Packaging /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/pos_2.12-1.4.1-tests.jar ...
[info] Done packaging.
[info] Running my.pkg.Main 
5       at my.pkg.Main.main(Main.scala:49)
Hello   at my.pkg.Main.main(Main.scala:50)
World   at my.pkg.Main.main(Main.scala:52)
6       at my.pkg.Main.main(Main.scala:53)
This will contain a compiler generated stack trace      at my.pkg.Main.main(Main.scala:55)
This line will not contain a compiler generated stack trace.
(one + two) -> 3        at my.pkg.Main.main(Main.scala:59)
(three * four) -> 12    at my.pkg.Main.main(Main.scala:63)
"(three == four) -> false" in thread run-main-0:
        at my.pkg.Main$.main(Main.scala:67) [pos_2.12-1.4.1-tests.jar]
        at my.pkg.Main.main(Main.scala) [pos_2.12-1.4.1-tests.jar]
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at sbt.Run.invokeMain(Run.scala:93)
        at sbt.Run.run0(Run.scala:87)
        at sbt.Run.execute$1(Run.scala:65)
        at sbt.Run.$anonfun$run$4(Run.scala:77)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:12) [scala-library.jar]
        at sbt.util.InterfaceUtil$$anon$1.get(InterfaceUtil.scala:10)
        at sbt.TrapExit$App.run(TrapExit.scala:252)
        at java.lang.Thread.run(Thread.java:748)
^ The above stack trace leads to an assertion failure. ^
"Three must not equal four" in thread run-main-0:
        at my.pkg.Main$.main(Main.scala:68) [pos_2.12-1.4.1-tests.jar]
        at my.pkg.Main.main(Main.scala) [pos_2.12-1.4.1-tests.jar]
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at sbt.Run.invokeMain(Run.scala:93)
        at sbt.Run.run0(Run.scala:87)
        at sbt.Run.execute$1(Run.scala:65)
        at sbt.Run.$anonfun$run$4(Run.scala:77)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:12) [scala-library.jar]
        at sbt.util.InterfaceUtil$$anon$1.get(InterfaceUtil.scala:10)
        at sbt.TrapExit$App.run(TrapExit.scala:252)
        at java.lang.Thread.run(Thread.java:748)
^ The above stack trace leads to an assertion failure. ^
null    at my.pkg.Main.runNullSafetyTest(Main.scala:80)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:82)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:84)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:86)
null
null    at my.pkg.Main.runNullSafetyTest(Main.scala:90)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:92)
[success] Total time: 7 s, completed Oct 4, 2018 9:45:51 PM
 */

/*
Output if environment variable DISABLE_POS_DEBUG is set:
username$ export DISABLE_POS_DEBUG=true
username$ sbt test:run
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/username/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/username/Downloads/scala-trace-debug/5.0/project
[info] Loading settings from build.sbt ...
[info] Set current project to pos (in build file:/Users/username/Downloads/scala-trace-debug/5.0/)
[info] Compiling 1 Scala source to /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/test-classes ...
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:49:8: comparing values of types Int and Null using `==' will always yield false
[warn]     out(5)
[warn]        ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:53:8: comparing values of types Int and Null using `==' will always yield false
[warn]     err(6)
[warn]        ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:59:12: comparing values of types Int and Null using `==' will always yield false
[warn]     codeOut(one + two)
[warn]            ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:63:12: comparing values of types Int and Null using `==' will always yield false
[warn]     codeErr(three * four)
[warn]            ^
[warn] four warnings found
[info] Done compiling.
[info] Packaging /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/pos_2.12-1.4.1-tests.jar ...
[info] Done packaging.
[info] Running my.pkg.Main
This will contain a compiler generated stack trace
This line will not contain a compiler generated stack trace.
null
[success] Total time: 7 s, completed Oct 4, 2018 9:42:11 PM
 */