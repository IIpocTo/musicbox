lazy val root = (project in file(".")).settings(
  name := "musicbox-backend",
  scalaVersion := "2.12.5",
  version := "0.1",
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-language:_",
    "-target:jvm-1.8",
    "-encoding",
    "utf8"
  ),
  mainClass := Some("musicbox.Server"),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.5.11",
    "com.typesafe.akka" %% "akka-stream" % "2.5.11",
    "com.typesafe.akka" %% "akka-slf4j" % "2.5.11",
    "com.typesafe.akka" %% "akka-http" % "10.1.0",
    "org.reactivemongo" %% "reactivemongo" % "0.13.0",
    "io.circe" %% "circe-core" % "0.9.3",
    "io.circe" %% "circe-generic" % "0.9.3",
    "io.circe" %% "circe-parser" % "0.9.3",
    "de.heikoseeberger" %% "akka-http-circe" % "1.20.0",
    "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.11" % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % "10.1.0" % Test
  ),
  scalafmtOnCompile in ThisBuild := true
)
