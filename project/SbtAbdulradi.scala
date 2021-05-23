import sbt.Keys._
import sbt.{Def, _}
import sbtghactions.GenerativePlugin.autoImport._

/* My default sane sbt settings for Scala 3 projects */
object SbtAbdulradiProjectPlugin extends AutoPlugin {
  override def trigger: PluginTrigger = allRequirements

  override def projectSettings = inThisBuild(Seq(
    organization := "com.abdulradi",
    licenses     := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    developers := List(Developer(
      id    = "tabdulradi",
      name  = "Tamer Abdulradi",
      email = "tamer@abdulradi.com",
      url   = url("http://abdulradi.com")
    )),

    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowPublishTargetBranches := Seq(RefPredicate.StartsWith(Ref.Tag("v"))),
    githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release"))),
    githubWorkflowPublish := Seq(
      WorkflowStep.Sbt(
        List("ci-release"),
        env = Map(
          "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
          "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
          "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
          "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
        )
      )
    ),

  )) ++ Seq(

    scalacOptions ++= Seq(
      // "-explain",
      "-source:future",
      "-Yexplicit-nulls", // Make reference types non-validated. validated types can be expressed with unions: e.g. String|Null.
      "-Ycook-comments", // type checks `@usecase` sections of docstrings
      "-Ysafe-init", // https://github.com/lampepfl/dotty/blob/master/docs/docs/reference/other-new-features/safe-initialization.md
      // "-Yinstrument", // Add instrumentation code that counts allocations and closure creations.
      // "-Yinstrument-defs", // Add instrumentation code that counts method calls;
      // "-Yprofile-enabled"
    )
  )
}