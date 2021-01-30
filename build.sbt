scalaVersion := "2.13.3"

scalacOptions += "-Ymacro-annotations"

libraryDependencies ++= BuildConfig.projectDependencies

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")