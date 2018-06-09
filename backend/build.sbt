lazy val akkaVersion = "2.5.12"
lazy val akkaHttpVersion = "10.1.1"
lazy val macwireVersion = "2.3.1"

lazy val root = (project in file(".")).settings(
  name := "musicbox-backend",
  scalaVersion := "2.12.6",
  version := "0.1",
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-language:_",
    "-target:jvm-1.8",
    "-Ypartial-unification",
    "-encoding",
    "utf8"
  ),
  mainClass in (Compile, run) := Some("musicbox.Server"),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "ch.megard" %% "akka-http-cors" % "0.3.0",
    "org.typelevel" %% "cats-core" % "1.1.0",
    "org.reactivemongo" %% "reactivemongo" % "0.13.0",
    "org.json4s" %% "json4s-jackson" % "3.6.0-M4",
    "org.json4s" %% "json4s-ext" % "3.6.0-M4",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.21.0",
    "com.softwaremill.macwire" %% "macros" % macwireVersion % "provided",
    "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % "provided",
    "com.softwaremill.macwire" %% "util" % macwireVersion,
    "com.softwaremill.macwire" %% "proxy" % macwireVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
  )
)
