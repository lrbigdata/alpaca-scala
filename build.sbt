name := "alpaca-scala"

version := "3.0.0"

// POM settings for Sonatype
import xerial.sbt.Sonatype._
organization := "com.cynance"
homepage := Some(url("https://github.com/cynance/alpaca-scala"))
sonatypeProjectHosting := Some(GitHubHosting("cynance", "alpaca-scala", "devs@cynance.com"))
scmInfo := Some(ScmInfo(url("https://github.com/cynance/alpaca-scala"),"git@github.com:cynance/alpaca-scala.git"))
developers := List(Developer("cynance",
  "cynance",
  "devs@cynance.com",
  url("https://github.com/cynance")))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true

// Add sonatype repository settings
publishTo := sonatypePublishTo.value

scalaVersion := "2.12.8"

val circeVersion = "0.10.0"
val hammockVersion = "0.8.6"

libraryDependencies ++= Seq(
  "com.pepegar" %% "hammock-core" % hammockVersion,
  "com.pepegar" %% "hammock-circe" % hammockVersion,
  "com.github.pureconfig" %% "pureconfig" % "0.10.1",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.scalamock" %% "scalamock" % "4.1.0" % Test,
  "com.typesafe.akka" %% "akka-http"   % "10.1.7",
  "com.typesafe.akka" %% "akka-stream" % "2.5.19", // or whatever the latest version is,
  "io.nats" % "jnats" % "2.2.0",
  "org.typelevel" %% "cats-core" % "1.5.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided",
  "com.softwaremill.macwire" %% "macrosakka" % "2.3.3" % "provided",
  "com.softwaremill.macwire" %% "util" % "2.3.3",
  "com.softwaremill.macwire" %% "proxy" % "2.3.3",
  "com.beachape" %% "enumeratum" % "1.5.13",
  "com.beachape" %% "enumeratum-circe" % "1.5.21",
  "org.mockito" %% "mockito-scala-scalatest" % "1.5.12"
)


libraryDependencies += {
  val version = scalaBinaryVersion.value match {
    case "2.10" => "1.0.3"
    case _ ⇒ "1.6.2"
  }
  "com.lihaoyi" % "ammonite" % version % "test" cross CrossVersion.full
}

sourceGenerators in Test += Def.task {
  val file = (sourceManaged in Test).value / "amm.scala"
  IO.write(file, """object amm extends App { ammonite.Main.main(args) }""")
  Seq(file)
}.taskValue

coverageExcludedPackages := ".*ConfigService.*;.*Config.*;alpaca\\.client\\..*"

coverageEnabled := true
