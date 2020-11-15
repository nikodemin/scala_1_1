import sbt._

object BuildConfig {

  object versions {
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

    val zio = "1.0.3"
    val `zio-logging` = "0.5.2"
    val `zio-interop-cats` = "2.2.0.1"
  }

  val testDependencies = Seq(
    "org.scalactic" %% "scalactic" % versions.scalactic,
    "org.scalatest" %% "scalatest" % versions.scalatest,
    "org.scalacheck" %% "scalacheck" % versions.scalacheck,
    "org.scalatestplus" %% "scalacheck-1-14" % versions.`scalacheck-1-14`,
    "org.scalamock" %% "scalamock" % versions.scalamock,
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % versions.`scalacheck-shapeless_1.14`
  ).map(_ % Test)

  val circeDependencies = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % versions.circe)

  val tapirDependencies = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml",
    "com.softwaremill.sttp.tapir" %% "tapir-zio",
    "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server",
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s"
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

  val zioDependencies = Seq(
    "dev.zio" %% "zio" % versions.zio,
    "dev.zio" %% "zio-test" % versions.zio % Test,
    "dev.zio" %% "zio-test-sbt" % versions.zio % Test,
    "dev.zio" %% "zio-test-magnolia" % versions.zio % Test,
    "dev.zio" %% "zio-logging" % versions.`zio-logging`,
    "dev.zio" %% "zio-logging-slf4j" % versions.`zio-logging`,
    "dev.zio" %% "zio-interop-cats" % versions.`zio-interop-cats`
  )

  val projectDependencies: Seq[ModuleID] = testDependencies ++ circeDependencies ++
    slickDependencies ++ dbDependencies ++ logDependencies ++ tapirDependencies ++ zioDependencies
}
