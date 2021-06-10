/*
 * Copyright 2021 Tamer Abdulradi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

lazy val `cats-parse` = project.dependsOn(core).settings(
  name := "validated-cats-parse",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-parse" % "0.3.4"
  )
)

lazy val root = (project in file("."))
  .aggregate(core, `cats-parse`)
  .settings(
    name := "validated",
    publish / skip := true
  )
