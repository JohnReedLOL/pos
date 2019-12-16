# pos: print debugging on steroids

**pos**, short for position, is a hyperlink based print debugging library designed to work with any IDE or text editor that supports stack trace highlighting. Using compile-time macros in Scala to extract file names and line numbers, pos makes your print statements, assertions, and log statements easier to locate. Use it to append a "smart" hyperlink to your sourcecode, avoiding the need to "grep" or use "Ctr + F" to locate print statements. Use pos instead of System.out.println or System.err.println. Also supports logging.

[![Build Status](https://travis-ci.com/JohnReedLOL/pos.svg?branch=master)](https://travis-ci.com/JohnReedLOL/pos) [![Join the chat at https://gitter.im/scalaPos/Lobby](https://badges.gitter.im/scalaPos/Lobby.svg)](https://gitter.im/scalaPos/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

____________________________________________________________________________________________________________________

Table of Contents

* <a href="#Locate-Print-Statements">Locate Print Statements</a>
* <a href="#Logging">Logging</a>
* <a href="#Getting-Started">Getting Started</a>
* <a href="#Instructions">Instructions for IDEA</a>
* <a href="#Scala-Examples">Scala Examples</a>
* <a href="#Developers-Guide">Developer's Guide</a>
* <a href="#Building">Building</a>
* <a href="#License">License</a>

____________________________________________________________________________________________________________________


<a name="Locate-Print-Statements"></a>

### Locate Print Statements:

![Append Position](https://i.imgur.com/Mf9zST9.png)

^ Clicking on the compile-time generated file name and line number will cause you to jump to that line in your source code.

Better than using println! Also, it is safe to pass in null.

Implementation:

```
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
          _root_.scala.Console.println($myString + " - " + $path + "(" + $fileName + ":" + $lineNum + ")");
      """
      c.Expr[Unit](toReturn)
    }
  }
```

____________________________________________________________________________________________________________________

<a name="Logging"></a>

### Logging:

Use "TraceLogging" instead of "StrictLogging", as in: 
- https://github.com/JohnReedLOL/pos/blob/master/src/test/scala/my/pkg/Main.scala 

![TraceLogger](https://i.imgur.com/pG3s3hI.png)

Or you can wrap your logger in a helper method like so to capture the compile time stack trace:

![Logger](https://i.imgur.com/wkXxbCd.png)

Or manually append the compile time stack trace as a String:

`logger.warn("This is a warning" + pos())`

This functionality "does not rely on runtime reflection or stack inspection, and is done at compile-time using macros. This means that it is both orders of magnitude faster than e.g. getting file-name and line-numbers using stack inspection, and also works on Scala.js where reflection and stack inspection can't be used." - taken from Li Haoyi's *sourcecode*

____________________________________________________________________________________________________________________

<a name="Getting-Started"></a>

### Getting Started:

pos is available through [sbt bintray](https://bintray.com/johnreed2/maven/pos).

It was packaged and published like so:

[Publish 1.4.0](https://gist.githubusercontent.com/JohnReedLOL/ee707f7900938679a1b23f069565c899/raw/ffd583128890cab48ef9a7f106b432213bb9abf3/publish-1.4.0.txt) (No master shutoff)

[Publish 2.0.0](https://gist.githubusercontent.com/JohnReedLOL/b34c10ae91f547823d3a65e0a79e3023/raw/40492342884a315da48eb402461f663c89ce2476/publish-2.0.0.txt) (Includes master shutoff)

[Publish 2.1.0](https://gist.githubusercontent.com/JohnReedLOL/70f1e17a9ceb338140b27cd90eb78841/raw/b129b8425080f8f33152ef4edd938c775f7c210a/publish-2.1.0.txt) (Includes implicit parameter for logging)

[Publish 2.1.1](https://gist.github.com/JohnReedLOL/949ae6dd7e3186fe00f612790a19d7e7) changes the license to Apache 2.0 to provide the option of protection against patent claims.

[Publish 2.2.0](https://github.com/sbt/sbt-bintray/issues/164)
Removes the environment variable (master shutoff) and creates a TraceLogging trait that can be used instead of StrictLogging.

pos is a stripped down version of [scala-trace-debug](https://github.com/JohnReedLOL/scala-trace-debug).

Add these two lines to your build.sbt:

`resolvers += Resolver.bintrayRepo("johnreed2","maven")`

`libraryDependencies += "com.github.johnreedlol" %% "pos" % <most recent version as seen in:` [build.sbt](build.sbt) `>`

To avoid having deprecated stuff, this library only works for Scala 2.10 and up.
____________________________________________________________________________________________________________________

<a name="Instructions"></a>

### Instructions (for IntelliJ IDE):

1. Add the appropriate dependency from **Getting Started**

2. import [com.github.johnreedlol._](src/main/scala/com/github/johnreedlol/package.scala)

3. Make sure that you have IntelliJ run configuration set up to run from the IntelliJ console

Run > Edit Configurations... > SBT Task

Click the green "+" (Add new configuration)

![Example](https://i.imgur.com/hQsYPDW.png)

- Place some calls to scala trace debug and click the green 'Debug' (Shift+F9) button and follow the stack traces in the console. 
 
- Use the IntelliJ console arrows to navigate up and down the stack traces.

![IntelliJ console](https://i.imgur.com/0reDRBO.png)

The way this is intended to be used is, assuming you don't have a debugger set up, you just click on your code and press **Command + Alt + Down** on Mac or **Ctr + Alt + Down** on Non-Mac to scroll through the print statements in the order in which they are executed. This is a convenient way to trace the execution of your code.

____________________________________________________________________________________________________________________

<a name="Scala-Examples"></a>

### Scala Example:

See [this file](src/test/scala/my/pkg/Main.scala) and run it yourself with "sbt test:run".

____________________________________________________________________________________________________________________

### Developer's Guide

<a name="Developers-Guide"></a>

1. git clone https://github.com/JohnReedLOL/pos master
2. cd master/
3. sbt compile
4. sbt test:run
5. sbt package

Advanced: 
```scala
$ sbt
[info] Loading project definition from /home/.../pos/project
[info] Set current project to pos (in build file:/home/.../pos/)
> + clean
> + compile
> + test:compile
> + test:run
> + package
```

* "+" means "cross-building"

____________________________________________________________________________________________________________________

<a name="Building"></a>

### Building:

Java 8 and up

____________________________________________________________________________________________________________________

<a name="License"></a>

### License:

[The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)

*pos* is currently licensed under the Apache 2.0 License, a permissive license that also protects against patent patent claims.
