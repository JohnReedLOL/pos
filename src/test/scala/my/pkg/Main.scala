package my.pkg

/**
  * Created by johnreed on 3/23/16. Run with sbt test:run
  */
@SuppressWarnings(Array("org.wartremover.warts.Equals")) // We use == and != instead of === and =!= because no cats
object Main {

  /**
    * To prevent output mangling
    */
  def sleep(): Unit = {
    val milliseconds = 60
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
    out.apply("Hello apply")
    sleep()
    err.apply("World apply")
    sleep()
    println("This will contain a compiler generated stack trace" + pos())
    println("This will also contain a compiler generated stack trace" + pos.apply())
    println("This line will not contain a compiler generated stack trace.")
    val one = 1
    val two = 2
    codeOut(one + two)
    sleep()
    val three = 3
    val four = 4
    codeErr(three * four)
    checkWithMessage(three != four, "Three must not equal four")
    // These two lines should be the same
    check(three != four)
    check.apply(three != four)
    // This should generate a stack trace
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
    out((null: String))
    sleep()
    err((null: String))
    sleep()
    out.apply((null: String))
    sleep()
    err.apply((null: String))
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
[info] Updating ...
[info] Done updating.
[info] Compiling 3 Scala sources and 1 Java source to /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/classes ...
[info] Done compiling.
[success] Total time: 12 s, completed Sep 27, 2018 10:22:42 AM
sbt:pos> test:compile
[info] Compiling 1 Scala source to /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/test-classes ...
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:25:8: comparing values of types Int and Null using `==' will always yield false
[warn]     out(5)
[warn]        ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:31:8: comparing values of types Int and Null using `==' will always yield false
[warn]     err(6)
[warn]        ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:45:12: comparing values of types Int and Null using `==' will always yield false
[warn]     codeOut(one + two)
[warn]            ^
[warn] /Users/username/Downloads/scala-trace-debug/5.0/src/test/scala/my/pkg/Main.scala:49:12: comparing values of types Int and Null using `==' will always yield false
[warn]     codeErr(three * four)
[warn]            ^
[warn] four warnings found
[info] Done compiling.
[success] Total time: 2 s, completed Sep 27, 2018 10:22:47 AM
sbt:pos> test:run
[info] Packaging /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/pos_2.12-1.3.0-tests.jar ...
[info] Packaging /Users/username/Downloads/scala-trace-debug/5.0/target/scala-2.12/pos_2.12-1.3.0.jar ...
[info] Done packaging.
[info] Done packaging.
[info] Running my.pkg.Main
5       at my.pkg.Main.main(Main.scala:25)
Hello   at my.pkg.Main.main(Main.scala:27)
World   at my.pkg.Main.main(Main.scala:29)
6       at my.pkg.Main.main(Main.scala:31)
Hello apply     at my.pkg.Main.main(Main.scala:33)
World apply     at my.pkg.Main.main(Main.scala:35)
This will contain a compiler generated stack trace      at my.pkg.Main.main(Main.scala:37)
This will also contain a compiler generated stack trace at my.pkg.Main.main(Main.scala:39)
This line will not contain a compiler generated stack trace.
(one + two) -> 3        at my.pkg.Main.main(Main.scala:45)
(three * four) -> 12    at my.pkg.Main.main(Main.scala:49)
"(three == four) -> false" in thread run-main-0:
        at my.pkg.Main$.main(Main.scala:57) [pos_2.12-1.3.0-tests.jar]
        at my.pkg.Main.main(Main.scala) [pos_2.12-1.3.0-tests.jar]
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
        at my.pkg.Main$.main(Main.scala:59) [pos_2.12-1.3.0-tests.jar]
        at my.pkg.Main.main(Main.scala) [pos_2.12-1.3.0-tests.jar]
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
null    at my.pkg.Main.runNullSafetyTest(Main.scala:69)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:71)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:73)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:75)
null
null    at my.pkg.Main.runNullSafetyTest(Main.scala:79)
null    at my.pkg.Main.runNullSafetyTest(Main.scala:81)
[success] Total time: 2 s, completed Sep 27, 2018 10:22:52 AM
sbt:pos> exit
[info] shutting down server

 */
