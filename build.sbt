ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "megogo"
  )

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % "0.23.23",
  "org.http4s" %% "http4s-client" % "0.23.23",
  "org.http4s" %% "http4s-circe" % "0.23.23",
  "org.http4s" %% "http4s-blaze-server" % "0.23.15",
  "org.http4s" %% "http4s-dsl" % "0.23.23",
  "io.circe" %% "circe-generic" % "0.14.6",
  "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.9.10",
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.9.10",
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.9.10",
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.9.10",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "org.http4s" %% "http4s-ember-client" % "0.23.23" % Test
)

assembly / assemblyMergeStrategy := {
  case "META-INF/maven/org.webjars/swagger-ui/pom.properties" => MergeStrategy.singleOrError
  case "META-INF/maven/org.webjars/swagger-ui/pom.xml" => MergeStrategy.singleOrError
  case "META-INF/resources/webjars/swagger-ui/4.15.5/swagger-ui.css" => MergeStrategy.first
  case "META-INF/resources/webjars/swagger-ui/4.15.5/swagger-ui-bundle.js" => MergeStrategy.first
  case "META-INF/resources/webjars/swagger-ui/4.15.5/swagger-ui-standalone-preset.js" => MergeStrategy.first
  case "META-INF/resources/webjars/swagger-ui/4.15.5/oauth2-redirect.html" => MergeStrategy.first
  case PathList("META-INF", "resources", "webjars", "swagger-ui", xs@_*) => MergeStrategy.first
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}