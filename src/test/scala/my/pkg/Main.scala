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

  def main(args: Array[String]) {
    out("Hello")
    sleep()
    err("World")
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
    codeOut(one + two)
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
  }
}

/*
Output:

ComputerName: username$ sbt
[info] Loading global plugins from /Users/username/.sbt/0.13/plugins
[info] Loading project definition from /Users/username/Downloads/scala-trace-debug/5.0/project
Missing bintray credentials /Users/username/.bintray/.credentials. Some bintray features depend on this.
[info] Set current project to scala-trace-debug (in build file:/Users/username/Downloads/scala-trace-debug/5.0/)
> compile
[warn] Credentials file /Users/username/.bintray/.credentials does not exist
[success] Total time: 0 s, completed Sep 24, 2018 6:12:01 PM
> test:run
[warn] Credentials file /Users/username/.bintray/.credentials does not exist
[info] Running my.pkg.Main
Hello
        at my.pkg.Main.main(Main.scala:16)
World
        at my.pkg.Main.main(Main.scala:18)
Hello apply
        at my.pkg.Main.main(Main.scala:20)
World apply
        at my.pkg.Main.main(Main.scala:22)
You have to put parenthesis on pos or else it will just print this: com.github.johnreedlol.package$pos$@6baf699a
This will contain a compiler generated stack trace
        at my.pkg.Main.main(Main.scala:25)
This will also contain a compiler generated stack trace
        at my.pkg.Main.main(Main.scala:27)
This line will not contain a compiler generated stack trace.
(one + two) -> 3
        at my.pkg.Main.main(Main.scala:33)
(three * four) -> 12
        at my.pkg.Main.main(Main.scala:37)
"(three == four) -> false" in thread run-main-0:
        at my.pkg.Main$.main(Main.scala:45)
        at my.pkg.Main.main(Main.scala)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at sbt.Run.invokeMain(Run.scala:67)
        at sbt.Run.run0(Run.scala:61)
        at sbt.Run.sbt$Run$$execute$1(Run.scala:51)
        at sbt.Run$$anonfun$run$1.apply$mcV$sp(Run.scala:55)
        at sbt.Run$$anonfun$run$1.apply(Run.scala:55)
        at sbt.Run$$anonfun$run$1.apply(Run.scala:55)
        at sbt.Logger$$anon$4.apply(Logger.scala:85)
        at sbt.TrapExit$App.run(TrapExit.scala:248)
        at java.lang.Thread.run(Thread.java:748)
^ The above stack trace leads to an assertion failure. ^
"Three must not equal four" in thread run-main-0:
        at my.pkg.Main$.main(Main.scala:47)
        at my.pkg.Main.main(Main.scala)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at sbt.Run.invokeMain(Run.scala:67)
        at sbt.Run.run0(Run.scala:61)
        at sbt.Run.sbt$Run$$execute$1(Run.scala:51)
        at sbt.Run$$anonfun$run$1.apply$mcV$sp(Run.scala:55)
        at sbt.Run$$anonfun$run$1.apply(Run.scala:55)
        at sbt.Run$$anonfun$run$1.apply(Run.scala:55)
        at sbt.Logger$$anon$4.apply(Logger.scala:85)
        at sbt.TrapExit$App.run(TrapExit.scala:248)
        at java.lang.Thread.run(Thread.java:748)
^ The above stack trace leads to an assertion failure. ^
[success] Total time: 1 s, completed Sep 24, 2018 6:12:08 PM
> exit
 */
