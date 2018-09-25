# pos: Better than System.out.println

**pos** is a hyperlink based print debugging system designed to work with any IDE or text editor that supports stack trace highlighting. Using compile-time macros in Scala to extract file names and line numbers, pos makes your print statements, assertions, and log statements easier to locate. Use it to append a "smart" hyperlink to your sourcecode, avoiding the need to "grep". Never use System.out/err.println again.

[![Build Status](https://travis-ci.com/JohnReedLOL/pos.svg?branch=master)](https://travis-ci.com/JohnReedLOL/pos)

____________________________________________________________________________________________________________________

Table of Contents

* <a href="#Locate-Statements">Locate Statements</a>
* <a href="#Getting-Started">Getting Started</a>
* <a href="#Instructions">Instructions for IDEA</a>
* <a href="#Scala-Examples">Scala Examples</a>
* <a href="#Building">Building</a>
* <a href="#Developers-Guide">Developer's Guide</a>

____________________________________________________________________________________________________________________


<a name="Locate-Statements"></a>

### Locate Statements:

![Append Position](https://i.imgur.com/hNTzyhJ.png)

^ Clicking on the compile-time generated file name and line number will cause you to jump to that line in your source code.

Better than using println!

Use it with a logger like so:

`logger.warn("This is a warning" + pos())`

"pos() does not rely on runtime reflection or stack inspection, and is done at compile-time using macros. This means that it is both orders of magnitude faster than e.g. getting file-name and line-numbers using stack inspection, and also works on Scala.js where reflection and stack inspection can't be used." - taken from Li Haoyi's *sourcecode*

____________________________________________________________________________________________________________________

<a name="Getting-Started"></a>

### Getting Started:

pos is available through [sbt bintray](https://bintray.com/johnreed2/maven/pos).

It was packaged and published like so: https://gist.github.com/JohnReedLOL/9512f8d91ac73332568ff82e6042b29a

pos is based on [scala-trace-debug](https://github.com/JohnReedLOL/scala-trace-debug).

Add this to your build.sbt:

```scala
resolvers += Resolver.bintrayRepo("johnreed2","maven")

libraryDependencies += "com.github.johnreedlol" %% "pos" % "1.0.0"

```

To avoid having deprecated stuff, this library only works for Scala 2.10 and up
____________________________________________________________________________________________________________________

<a name="Instructions"></a>

### Instructions (for IntelliJ IDE):

1. Add the appropriate dependency from **Getting Started**

2. import [com.github.johnreedlol._](src/main/scala/com/github/johnreedlol/package.scala)

3. Make sure that you have IntelliJ run configuration set up to run from the IntelliJ console

Run > Edit Configurations... > SBT Task

Click the green "+" (Add new configuration)

![Example](http://i.imgur.com/UPZAJHo.png)

- Place some calls to scala trace debug and click the green 'Debug' (Shift+F9) button and follow the stack traces in the console. 
 
- Use the IntelliJ console arrows to navigate up and down the stack traces.

![IntelliJ console](https://i.imgur.com/QO5Qs7X.png)

For smooth scrolling, use the keyboard shortcut **Ctr+Alt+Down**
____________________________________________________________________________________________________________________

<a name="Scala-Examples"></a>

##### Code Example:

```scala
    import com.github.johnreedlol._

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
```

```
Output:

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

```

##### ^ [Run it yourself](src/test/scala/my/pkg/Main.scala) with "sbt test:run" ^

____________________________________________________________________________________________________________________

### Developer's Guide

<a name="Developers-Guide"></a>

1. git clone https://github.com/JohnReedLOL/pos master
2. cd $projectDirectory
3. sbt test
4. sbt test:run
5. sbt package

Advanced: 
```scala
$ sbt
[info] Loading project definition from /home/.../pos/project
[info] Set current project to pos (in build file:/home/.../pos/)
> + clean
> + compile 
> + test:run
> + package
```

* "+" means "cross-building"

____________________________________________________________________________________________________________________

<a name="Building"></a>

### Building:

Java 8 and up
