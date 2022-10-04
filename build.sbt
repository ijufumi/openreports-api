val ScalatraVersion = "2.8.2"
val SlickVersion = "3.4.0"
val ScalaCacheVersion = "0.28.0"
val SttpVersion = "3.7.1"
val Json4sVersion = "4.0.5"
val SLF4JVersion = "2.0.3"
val LogbackVersion = "1.4.3"

ThisBuild / scalaVersion := "2.13.9"
ThisBuild / organization := "jp.ijufumi"
ThisBuild / pomIncludeRepository := { _ =>
  false
}

lazy val root = (project in file("."))
  .enablePlugins(JettyPlugin, FlywayPlugin)
  .settings(
    name := "Open Report API",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-json" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
      "org.slf4j" % "slf4j-api" % SLF4JVersion,
      "org.slf4j" % "jcl-over-slf4j" % SLF4JVersion,
      "org.eclipse.jetty" % "jetty-webapp" % "9.4.35.v20201120" % "container;compile",
      "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
      "org.postgresql" % "postgresql" % "42.4.0",
      "org.flywaydb" % "flyway-core" % "9.1.3",
      "com.google.inject" % "guice" % "5.1.0",
      "org.json4s" %% "json4s-jackson" % Json4sVersion,
      "org.json4s" %% "json4s-native" % Json4sVersion,
      "commons-codec" % "commons-codec" % "1.15",
      "com.auth0" % "auth0" % "1.42.0",
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
      "com.typesafe.slick" %% "slick-testkit" % SlickVersion % "test",
      "com.github.cb372" %% "scalacache-core" % ScalaCacheVersion exclude ("org.slf4j", "slf4j-api"),
      "com.github.cb372" %% "scalacache-caffeine" % ScalaCacheVersion exclude ("org.slf4j", "slf4j-api"),
      "com.github.cb372" %% "scalacache-cats-effect" % ScalaCacheVersion exclude ("org.slf4j", "slf4j-api"),
      "com.softwaremill.sttp.client3" %% "core" % SttpVersion,
      "com.softwaremill.sttp.client3" %% "json4s" % SttpVersion,
      "io.github.cdimascio" % "dotenv-java" % "2.2.4",
      "de.huxhorn.sulky" % "de.huxhorn.sulky.ulid" % "8.3.0",
      "org.jxls" % "jxls" % "2.12.0",
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "ch.qos.logback" % "logback-core" % LogbackVersion,
    ),
    assembly / assemblyJarName := "open-report-api.jar",
    assembly / mainClass := Some("JettyLauncher"),
  )

val dbHost = sys.env.getOrElse("DB_HOST", "localhost")
val dbName = sys.env.getOrElse("DB_NAME", "openreports")
val dbUser = sys.env.getOrElse("DB_USER", "postgres")
val dbPassword = sys.env.getOrElse("DB_PASSWORD", "password")
val dbPort = sys.env.getOrElse("DB_PORT", "5432")

flywayUrl := f"jdbc:postgresql://$dbHost%s:$dbPort%s/$dbName%s"
flywayUser := dbUser
flywayPassword := dbPassword
flywayBaselineOnMigrate := true
flywayBaselineVersion := "0"
flywaySchemas += "public"
