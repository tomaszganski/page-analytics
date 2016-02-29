name := """page-analytics"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0",
//  "com.h2database" % "h2" % "1.4.177",
  "org.xerial" % "sqlite-jdbc" % "3.8.11.2",
  "com.google.api-client" % "google-api-client-gson" % "1.21.0",
  "com.google.apis" % "google-api-services-analytics" % "v3-rev124-1.21.0",
    specs2 % Test
)     

routesGenerator := InjectedRoutesGenerator


fork in run := false