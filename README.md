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

____________________________________________________________________________________________________________________

<a name="Logging"></a>

### Logging:

Use "[TraceLogging](https://github.com/JohnReedLOL/pos/blob/master/src/main/scala/com/github/johnreedlol/logging/TraceLogging.scala)" instead of "StrictLogging". Click image to enlarge:

![TraceLogger](https://i.imgur.com/pG3s3hI.png)

Or you can wrap your logger in a helper method like so to capture the compile time stack trace:

![Logger](https://i.imgur.com/wkXxbCd.png)

Or manually append the compile time stack trace as a String:

`logger.warn("This is a warning" + pos())`

This functionality "does not rely on runtime reflection or stack inspection, and is done at compile-time using macros. This means that it is both orders of magnitude faster than e.g. getting file-name and line-numbers using stack inspection, and also works on Scala.js where reflection and stack inspection can't be used." - taken from Li Haoyi's *sourcecode*

____________________________________________________________________________________________________________________

<a name="Getting-Started"></a>

### Getting Started:

Add these two lines to your build.sbt:

`resolvers += Resolver.bintrayRepo("johnreed2","maven")`

`libraryDependencies += "com.github.johnreedlol" %% "pos" % "2.2.0"`

To avoid having deprecated stuff, this library only works for Scala 2.10 and up.

pos is a stripped down version of [scala-trace-debug](https://github.com/JohnReedLOL/scala-trace-debug).

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
