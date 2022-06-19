val ScalatraVersion = "2.8.2"

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "jp.ijufumi"

lazy val root = (project in file("."))
  .settings(
    name := "Open Report API",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "9.4.35.v20201120" % "container;compile",
      "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
      "org.postgresql" % "postgresql" % "42.4.0"
    ),
    assembly / assemblyJarName := "open-report-api.jar",
    assembly / mainClass := Some("JettyLauncher")
  )

lazy val liquibasePlugin = (project in file("liquibasePlugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "LiquibasePlugin",
    sbtPlugin := true,
    version := "0.1.0-SNAPSHOT",
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.2.8" // set minimum sbt version
      }
    },
    libraryDependencies ++= Seq(
      "org.liquibase" % "liquibase-core" % "4.11.0"
    )
  )

// because scripted-sbt does not support scala 2.13
liquibasePlugin / scalaVersion := "2.12.16"

enablePlugins(JettyPlugin)
