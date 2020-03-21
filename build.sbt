val jettyVersion = "9.4.12.v20180830"

organization := "com.github.zwrss"

name := "finder-adapter"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-server" % jettyVersion,
  "org.eclipse.jetty" % "jetty-servlet" % jettyVersion,
  "com.typesafe.play" %% "play-json" % "2.6.10",
  "org.asynchttpclient" % "async-http-client" % "2.10.4",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "org.apache.commons" % "commons-csv" % "1.8",
  "org.scalatest" %% "scalatest" % "3.1.0" % "test"
)

enablePlugins(JavaAppPackaging)
