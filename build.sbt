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
  organizationName := "mazboot",
  description      := "Companion to the Scala std lib, providing useful subtypes like PositiveInt or StringMatchingRegex. As well as the ability to define custom validations.",
  homepage         := Some(url("https://github.com/tabdulradi/mazboot")),
  scmInfo          := Some(ScmInfo(url("https://github.com/tabdulradi/mazboot"), "scm:git@github.com:tabdulradi/mazboot.git")),

  scalaVersion := "3.0.0",
  crossScalaVersions := Seq("3.0.0", "3.0.1-RC2")
))

lazy val core = project.settings(
  name := "mazboot-core",
  libraryDependencies += "com.abdulradi" %% "happypath-core" % "0.3.0"
)

lazy val types = project.dependsOn(core).settings(
  name := "mazboot-types"
)

lazy val `cats-parse` = project.dependsOn(core).settings(
  name := "mazboot-cats-parse",
  libraryDependencies += "org.typelevel" %% "cats-parse" % "0.3.4"
)

lazy val ciris = project.dependsOn(core).settings(
  name := "mazboot-ciris",
  libraryDependencies += "is.cir" %% "ciris" % "2.0.1"
)

lazy val tests = 
  project
    .dependsOn(types, `cats-parse`, ciris)
    .settings(
      name := "mazboot-tests",
      publish / skip := true,
      libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-effect" % "3.1.1"
      )
    )

lazy val root = 
  project
    .in(file("."))
    .aggregate(core, types, `cats-parse`, ciris, tests)
    .settings(
      name := "mazboot",
      publish / skip := true
    )
