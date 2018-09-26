package my.pkg

import com.github.johnreedlol._

/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
object Main {

  /**
    * To prevent output mangling
    */
  def sleep(): Unit = {
    val milliseconds = 60
    Thread.sleep(milliseconds)
  }

  def main(args: Array[String]): Unit = {
    out(5)
    sleep()
    out("Hello")
    sleep()
    err("World")
    sleep()
    err(6)
    sleep()
    out.apply("Hello apply")
    sleep()
    err.apply("World apply")
    sleep()
    println("You have to put parenthesis on pos or else it will just print this: " + pos.toString)
    println("This will contain a compiler generated stack trace" + pos())
    sleep()
    println("This will also contain a compiler generated stack trace" + pos.apply())
    sleep()
    println("This line will not contain a compiler generated stack trace.")
    sleep()
    val one = 1
    val two = 2
    codeOut.apply(one + two)
    sleep()
    val three = 3
    val four = 4
    codeErr(three * four)
    sleep()
    check(three != four, "Three must not equal four")
    // These two lines should be the same
    check(three != four)
    check.apply(three != four)
    sleep()
    // This should generate a stack trace
    check(three == four)
    sleep()
    check(three == four, "Three must not equal four")
    sleep()
    runNullSafetyTest()
  }

  /**
    * pos must not throw a null pointer even if null is passed in.
    */
  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def runNullSafetyTest(): Unit = {
    out((null: String))
    sleep()
    err((null: String))
    sleep()
    out.apply((null: String))
    sleep()
    err.apply((null: String))
    sleep()
    println("You have to put parenthesis on pos or else it will just print this: " + pos.toString)
    println("This will contain a compiler generated stack trace" + pos())
    sleep()
    println("This will also contain a compiler generated stack trace" + pos.apply())
    sleep()
    println((null: String))
    sleep()
    codeOut.apply((null: String))
    sleep()
    codeErr.apply((null: String))
    sleep()
  }
}

/*
Output:

username$ sbt
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/username/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/username/Downloads/scala-trace-debug/5.0/project
[info] Loading settings from build.sbt ...
[info] Set current project to pos (in build file:/Users/username/Downloads/scala-trace-debug/5.0/)
[info] sbt server started at local:///Users/username/.sbt/1.0/server/62a082dda9271e30c434/sock
sbt:pos> compile
[info] Compiling 3 Scala sources and 1 Java source to /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/classes ...
[info] Done compiling.
[success] Total time: 13 s, completed Sep 26, 2018 2:42:03 PM
sbt:pos> test:compile
[info] Compiling 1 Scala source to /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/test-classes ...
[info] Done compiling.
[success] Total time: 1 s, completed Sep 26, 2018 2:42:15 PM
sbt:pos> test:run
[info] Packaging /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/pos_2.12-1.1.0-tests.jar ...
[info] Packaging /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/pos_2.12-1.1.0.jar ...
[info] Done packaging.
[info] Done packaging.
[info] Running my.pkg.Main
5       at my.pkg.Main.main(Main.scala:19)
Hello   at my.pkg.Main.main(Main.scala:21)
World   at my.pkg.Main.main(Main.scala:23)
6       at my.pkg.Main.main(Main.scala:25)
Hello apply     at my.pkg.Main.main(Main.scala:27)
World apply     at my.pkg.Main.main(Main.scala:29)
You have to put parenthesis on pos or else it will just print this: com.github.johnreedlol.package$pos$@59567797
This will contain a compiler generated stack trace      at my.pkg.Main.main(Main.scala:32)
This will also contain a compiler generated stack trace at my.pkg.Main.main(Main.scala:34)
This line will not contain a compiler generated stack trace.
(one + two) -> 3        at my.pkg.Main.main(Main.scala:40)
(three * four) -> 12    at my.pkg.Main.main(Main.scala:44)
"(three == four) -> false" in thread run-main-0:
        at my.pkg.Main$.main(Main.scala:52) [pos_2.12-1.1.0-tests.jar]
        at my.pkg.Main.main(Main.scala) [pos_2.12-1.1.0-tests.jar]
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
        at my.pkg.Main$.main(Main.scala:54) [pos_2.12-1.1.0-tests.jar]
        at my.pkg.Main.main(Main.scala) [pos_2.12-1.1.0-tests.jar]
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
null    at my.pkg.Main.runNullSafetyTest(Main.scala:64)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:66)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:68)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:70)
You have to put parenthesis on pos or else it will just print this: com.github.johnreedlol.package$pos$@59567797
This will contain a compiler generated stack trace      at my.pkg.Main.runNullSafetyTest(Main.scala:73)
This will also contain a compiler generated stack trace at my.pkg.Main.runNullSafetyTest(Main.scala:75)
null
null    at my.pkg.Main.runNullSafetyTest(Main.scala:79)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:81)
[success] Total time: 2 s, completed Sep 26, 2018 2:42:22 PM
sbt:pos> exit
[info] shutting down server
 */
