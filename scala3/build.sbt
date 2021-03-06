val scala3Version = "3.0.0"

lazy val root = project
  .in(file("."))
  .settings(
    name                := "square-root-scala3",
    version             := "1.0.0",
    scalaVersion        := scala3Version,
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
