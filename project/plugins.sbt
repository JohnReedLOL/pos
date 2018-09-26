addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.4")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

// Removes warts like null
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.3.7")

// resolvers += Resolver.sonatypeRepo("releases")

// addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

/** Error during compilation of source code that depends on macros that access the source code:
  *
  * [info] Setting version to 2.10.4
  * exception during macro expansion:
  * [error] java.lang.UnsupportedOperationException: Position.start on class scala.reflect.internal.util.OffsetPosition
  * [error]         at scala.reflect.internal.util.Position.start(Position.scala:114)
  *
  * As a workaround, you can pass -Dsbt.parser.simple=true to your play/SBT command line. This will revert to the old .sbt parser that splits based on \n\n.
  */