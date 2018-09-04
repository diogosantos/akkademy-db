name := "akkademy-db"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.11.12"

lazy val global = project
  .in(file("."))
  .aggregate(
    messages,
    server,
    client
  )

lazy val messages = project
  .settings(
    name := "messages"
  )

lazy val server = project
  .settings(
    name := "server",
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(
    messages
  )

lazy val client = project
  .settings(
    name := "client",
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(
    messages
  )


lazy val dependencies = new {
  val akkaV = "2.5.16"

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaV
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" % akkaV
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaV % "test"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.1.6" % "test"
}

lazy val commonDependencies = Seq(
  dependencies.akkaActor,
  dependencies.akkaRemote,
  dependencies.akkaTestKit,
  dependencies.scalaTest
)
