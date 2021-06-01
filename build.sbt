inThisBuild(Seq(
  organizationName := "validated",
  description      := "Companion to the Scala std lib, providing useful subtypes like PositiveInt or StringMatchingRegex. As well as the ability to define custom validations.",
  homepage         := Some(url("https://github.com/tabdulradi/validated")),
  scmInfo          := Some(ScmInfo(url("https://github.com/tabdulradi/validated"), "scm:git@github.com:tabdulradi/validated.git")),

  scalaVersion := "3.0.0",
))

lazy val core = (project in file("core")).settings(
  name := "validated-core",
  libraryDependencies ++= Seq(
    "com.abdulradi" %% "happypath-core" % "0.3.0"
  )
)//.dependsOn(happypath)
// lazy val happypath = ProjectRef(file("../happypath"), "core")

lazy val root = (project in file("."))
  .aggregate(core)
  .settings(
    name := "validated",
    publish / skip := true
  )
