import sbt._

object BuildConfig {

  object versions {
    val akka = "2.6.9"
    val `akka-http` = "10.2.0"

    val circe = "0.14.0-M1"

    val tapir = "0.17.0-M2"

    val scalactic = "3.2.0"
    val scalatest = "3.2.0"
    val scalacheck = "1.14.3"
    val `scalacheck-1-14` = "3.2.0.0"
    val scalamock = "5.0.0"

    val slick = "3.3.3"
    val postgresql = "42.2.15"
    val `flyway-core` = "6.5.5"

    val slf4j = "1.7.30"
    val logback = "1.2.3"

    val `scalacheck-shapeless_1.14` = "1.2.3"
  }

  val testDependencies = Seq(
    "org.scalactic" %% "scalactic" % versions.scalactic,
    "org.scalatest" %% "scalatest" % versions.scalatest,
    "org.scalacheck" %% "scalacheck" % versions.scalacheck,
    "org.scalatestplus" %% "scalacheck-1-14" % versions.`scalacheck-1-14`,
    "org.scalamock" %% "scalamock" % versions.scalamock,
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % versions.`scalacheck-shapeless_1.14`
  ).map(_ % Test)

  val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-stream" % versions.akka,
    "com.typesafe.akka" %% "akka-http" % versions.`akka-http`,
    "com.typesafe.akka" %% "akka-stream-testkit" % versions.akka,
    "com.typesafe.akka" %% "akka-http-testkit" % versions.`akka-http`,
    "com.typesafe.akka" %% "akka-actor-typed" % versions.akka
  )

  val circeDependencies = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % versions.circe)

  val tapirDependencies = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server",
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml"
  ).map(_ % versions.tapir)

  val slickDependencies = Seq(
    "com.typesafe.slick" %% "slick" % versions.slick,
    "com.typesafe.slick" %% "slick-hikaricp" % versions.slick
  )

  val dbDependencies = Seq(
    "org.postgresql" % "postgresql" % versions.postgresql,
    "org.flywaydb" % "flyway-core" % versions.`flyway-core`
  )

  val logDependencies = Seq(
    "org.slf4j" % "slf4j-api" % versions.slf4j,
    "ch.qos.logback" % "logback-classic" % versions.logback
  )

  val projectDependencies: Seq[ModuleID] = testDependencies ++ akkaDependencies ++ circeDependencies ++
    slickDependencies ++ dbDependencies ++ logDependencies ++ tapirDependencies
}
