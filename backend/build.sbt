lazy val root = (project in file(".")).
  settings(
    name := "musicbox-backend",
    scalaVersion := "2.12.4",
    version := "0.1",
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    mainClass := Some("musicbox.Server"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.5.9",
      "com.typesafe.akka" %% "akka-stream" % "2.5.9",
      "com.typesafe.akka" %% "akka-http" % "10.0.11",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.9" % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.9" % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.11" % Test
    )
  )
