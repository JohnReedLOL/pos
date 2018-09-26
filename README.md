# pos: print debugging on steroids

**pos**, short for position, is a hyperlink based print debugging system designed to work with any IDE or text editor that supports stack trace highlighting. Using compile-time macros in Scala to extract file names and line numbers, pos makes your print statements, assertions, and log statements easier to locate. Use it to append a "smart" hyperlink to your sourcecode, avoiding the need to "grep" or use "Ctr + F" to locate print statements. Use pos instead of System.out.println or System.err.println.

[![Build Status](https://travis-ci.com/JohnReedLOL/pos.svg?branch=master)](https://travis-ci.com/JohnReedLOL/pos) [![Join the chat at https://gitter.im/scalaPos/Lobby](https://badges.gitter.im/scalaPos/Lobby.svg)](https://gitter.im/scalaPos/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

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

![Append Position](https://i.imgur.com/JHxkGlH.png)

^ Clicking on the compile-time generated file name and line number will cause you to jump to that line in your source code.

Better than using println! Also, it is safe to pass in null.

Use it with a logger like so:

`logger.warn("This is a warning" + pos())`

"pos() does not rely on runtime reflection or stack inspection, and is done at compile-time using macros. This means that it is both orders of magnitude faster than e.g. getting file-name and line-numbers using stack inspection, and also works on Scala.js where reflection and stack inspection can't be used." - taken from Li Haoyi's *sourcecode*

____________________________________________________________________________________________________________________

<a name="Getting-Started"></a>

### Getting Started:

pos is available through [sbt bintray](https://bintray.com/johnreed2/maven/pos).

It was packaged and published like so:

[Publish 1.1.0](https://gist.githubusercontent.com/JohnReedLOL/670b7c88d7e0e5137b0a11960e994cf9/raw/33187581996501c27d0ed153fcf5fd787b97a6df/publish1.1.0.txt)

[Publish 1.2.0](https://gist.githubusercontent.com/JohnReedLOL/e8d43e59bb296dea923da335b5f6fe39/raw/4b0108345074f567c84b2becb31248376a5dca71/publish_1.2.0.txt)

pos is a stripped down version of [scala-trace-debug](https://github.com/JohnReedLOL/scala-trace-debug).

Add this to your build.sbt:

```scala
resolvers += Resolver.bintrayRepo("johnreed2","maven")

libraryDependencies += "com.github.johnreedlol" %% "pos" % <most recent version as seen in [build.sbt](build.sbt)>

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
