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
package my.pkg
import com.github.johnreedlol.logging.TraceLogging
/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main extends TraceLogging {

  /**
    * To prevent output mangling
    */
  def sleep(): Unit = {
    val milliseconds = 60L
    Thread.sleep(milliseconds)
  }

import com.github.johnreedlol.implicits.Pos
@SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter"))
def doLogging(message: String)(implicit position: Pos): Unit = {
  println(message + position.text)
}
doLogging("FooBar") // FooBar  at my.pkg.Main.<local Main>(Main.scala:47)

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def main(args: Array[String]): Unit = {
    // This tests the logging functionality:
    trace("Test0")
    debug("Test1")
    info("Test2")
    warn("Test3")
    warn(null)
    sleep()
    error("Test4")
    error(null)
    sleep()
    /*
     * Note: `out(5)` emits "comparing values of types Int and Null using `==' will always yield false"
     * This was fixed in version 1.2.0, but that fix required method overloading and caused problems when passing in values of type "Any"
     * So I removed that fix in version 1.3.0
     */
    import com.github.johnreedlol._
    doLogging("Foo bar") // Foo bar at my.pkg.Main.main(Main.scala:56)
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
    // This should generate a stack trace.
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
Output:
username$ sbt test:run
...
[info] Packaging /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/pos_2.12-2.1.0-tests.jar ...
[info] Packaging /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/pos_2.12-2.1.0.jar ...
[info] Done packaging.
[info] Done packaging.
[info] Running my.pkg.Main
FooBar  at my.pkg.Main.<local Main>(Main.scala:47)
Foo bar at my.pkg.Main.main(Main.scala:56)
5       at my.pkg.Main.main(Main.scala:57)
Hello   at my.pkg.Main.main(Main.scala:58)
World   at my.pkg.Main.main(Main.scala:60)
6       at my.pkg.Main.main(Main.scala:61)
This will contain a compiler generated stack trace      at my.pkg.Main.main(Main.scala:63)
This line will not contain a compiler generated stack trace.
(one + two) -> 3        at my.pkg.Main.main(Main.scala:67)
(three * four) -> 12    at my.pkg.Main.main(Main.scala:71)
"(three == four) -> false" in thread run-main-0:
        at my.pkg.Main$.main(Main.scala:75) [pos_2.12-2.1.0-tests.jar]
        at my.pkg.Main.main(Main.scala) [pos_2.12-2.1.0-tests.jar]
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
        at my.pkg.Main$.main(Main.scala:76) [pos_2.12-2.1.0-tests.jar]
        at my.pkg.Main.main(Main.scala) [pos_2.12-2.1.0-tests.jar]
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
null    at my.pkg.Main.runNullSafetyTest(Main.scala:88)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:90)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:92)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:94)
null
null    at my.pkg.Main.runNullSafetyTest(Main.scala:98)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:100)
[success] Total time: 1 s, completed Oct 5, 2018 2:49:39 AM
 */

/*
Output:
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