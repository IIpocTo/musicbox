lazy val root = (project in file(".")).
  settings(
    name := "musicbox-backend",
    scalaVersion := "2.12.4",
    version := "0.1",
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    mainClass := Some("musicbox.Server"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.11",
      "com.typesafe.akka" %% "akka-stream" % "2.5.11",
      "com.typesafe.akka" %% "akka-slf4j" % "2.5.11",
      "com.typesafe.akka" %% "akka-http" % "10.1.0-RC2",
      "org.reactivemongo" %% "reactivemongo" % "0.13.0",
      "io.circe" %% "circe-core" % "0.9.1",
      "io.circe" %% "circe-generic" % "0.9.1",
      "io.circe" %% "circe-parser" % "0.9.1",
      "de.heikoseeberger" %% "akka-http-circe" % "1.20.0-RC2",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.11" % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.0-RC2" % Test
    )
  )
