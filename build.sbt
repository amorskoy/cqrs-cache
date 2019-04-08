name := "cqrs-cache"

version := "0.1"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.22"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "io.swaydb" %% "swaydb" % "0.8-beta.7"
)