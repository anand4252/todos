
inThisBuild(
  List(
    organization := "com.todos",
    scalaVersion := "2.13.4",
  )
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    Compile / unmanagedSourceDirectories := (Compile / scalaSource).value :: Nil,
    Test / unmanagedSourceDirectories := (Test / scalaSource).value :: Nil,
    Compile / unmanagedResourceDirectories +=  sourceDirectory.value / "main" / "resources-scala",
    Compile / unmanagedResourceDirectories += baseDirectory.value / "web" / "dist",
    libraryDependencies ++= Dependencies.App,
    name := "todos.com",
    version := "0.0.1-SNAPSHOT",
    scalacOptions in ThisBuild := Options.scalacOptions(scalaVersion.value, isSnapshot.value),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
