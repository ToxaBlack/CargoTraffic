name := """CargoTraffic"""

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  evolutions,
  "org.hibernate" % "hibernate-entitymanager" % "5.0.6.Final",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "org.apache.commons" % "commons-collections4" % "4.1",
  "be.objectify" %% "deadbolt-java" % "2.4.3",
  "io.jsonwebtoken" % "jjwt" % "0.6.0",
  "de.svenkubiak" % "jBCrypt" % "0.4",
  "com.typesafe.play" %% "play-mailer" % "4.0.0-M1"
)

routesGenerator := InjectedRoutesGenerator
PlayKeys.externalizeResources := false
