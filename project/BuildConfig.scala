import sbt._

object BuildConfig {

  object versions {
    val akka = "2.6.8"
    val `akka-http` = "10.2.0"

    val `akka-http-play-json` = "1.34.0"
    val `play-json` = "2.9.0"

    val scalactic = "3.2.0"
    val scalatest = "3.2.0"
    val scalacheck = "1.14.3"
    val `scalacheck-1-14` = "3.2.0.0"
    val scalamock = "5.0.0"

    val slick = "3.3.3"
    val postgresql = "42.2.15"
    val `flyway-core` = "6.5.5"

    val slf4j = "1.7.30"

    val `scalacheck-shapeless_1.14` = "1.2.3"
  }

  val testDependencies = Seq(
    "org.scalactic" %% "scalactic" % versions.scalactic % Test,
    "org.scalatest" %% "scalatest" % versions.scalatest % Test,
    "org.scalacheck" %% "scalacheck" % versions.scalacheck % Test,
    "org.scalatestplus" %% "scalacheck-1-14" % versions.`scalacheck-1-14` % Test,
    "org.scalamock" %% "scalamock" % versions.scalamock % Test,
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % versions.`scalacheck-shapeless_1.14` % Test
  )

  val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-stream" % versions.akka,
    "com.typesafe.akka" %% "akka-http" % versions.`akka-http`,
    "com.typesafe.akka" %% "akka-stream-testkit" % versions.akka,
    "com.typesafe.akka" %% "akka-http-testkit" % versions.`akka-http`
  )

  val playJsonDependencies = Seq(
    "de.heikoseeberger" %% "akka-http-play-json" % versions.`akka-http-play-json`,
    "com.typesafe.play" %% "play-json" % versions.`play-json`
  )

  val slickDependencies = Seq(
    "com.typesafe.slick" %% "slick" % versions.slick,
    "com.typesafe.slick" %% "slick-hikaricp" % versions.slick
  )

  val dbDependencies = Seq(
    "org.postgresql" % "postgresql" % versions.postgresql,
    "org.flywaydb" % "flyway-core" % versions.`flyway-core`
  )

  val logDependencies = Seq(
    "org.slf4j" % "slf4j-api" % versions.slf4j
  )

  val projectDependencies: Seq[ModuleID] = testDependencies ++ akkaDependencies ++ playJsonDependencies ++
    slickDependencies ++ dbDependencies ++ logDependencies
}
