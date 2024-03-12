val Scala213Version = "2.13.9"
val Scala212Version = "2.12.18"

// for scala libraries
val ScalatraVersion = "2.8.4"
val SlickVersion = "3.4.1"
val ScalaCacheVersion = "0.28.0"
val SttpVersion = "3.8.15"
val Json4sVersion = "4.0.6"
val ULIDVersion = "1.0.24"

// for java libraries
val SLF4JVersion = "2.0.7"
val LogbackVersion = "1.4.7"
val FlywayVersion = "9.22.3"
val AWSSDKVersion = "2.20.76"
val jXlsVersion = "2.12.0"
val Auth0Version = "2.3.0"
val GuiceVersion = "6.0.0"
val DotEnvJavaVersion = "3.0.0"
val JettyVersion = "10.0.18"
val PostgresVersion = "42.6.0"
val ServletAPIVersion = "4.0.1"

ThisBuild / scalaVersion := Scala212Version
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
      "org.scalatra" %% "scalatra-forms" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion % "runtime",
      "org.slf4j" % "slf4j-api" % SLF4JVersion,
      "org.slf4j" % "jcl-over-slf4j" % SLF4JVersion,
      "org.eclipse.jetty" % "jetty-webapp" % JettyVersion % "container;compile",
      "javax.servlet" % "javax.servlet-api" % ServletAPIVersion % "provided",
      "org.postgresql" % "postgresql" % PostgresVersion,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
      "com.google.inject" % "guice" % GuiceVersion,
      "com.google.inject.extensions" % "guice-servlet" % GuiceVersion,
      "org.json4s" %% "json4s-jackson" % Json4sVersion,
      "org.json4s" %% "json4s-native" % Json4sVersion,
      "org.json4s" %% "json4s-ext" % Json4sVersion,
      "commons-codec" % "commons-codec" % "1.15",
      "com.auth0" % "auth0" % Auth0Version,
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
      "com.typesafe.slick" %% "slick-testkit" % SlickVersion % "test",
      "com.github.cb372" %% "scalacache-core" % ScalaCacheVersion exclude ("org.slf4j", "slf4j-api"),
      "com.github.cb372" %% "scalacache-caffeine" % ScalaCacheVersion exclude ("org.slf4j", "slf4j-api"),
      "com.github.cb372" %% "scalacache-cats-effect" % ScalaCacheVersion exclude ("org.slf4j", "slf4j-api"),
      "com.softwaremill.sttp.client3" %% "core" % SttpVersion,
      "com.softwaremill.sttp.client3" %% "json4s" % SttpVersion,
      "software.amazon.awssdk" % "s3" % AWSSDKVersion,
      "io.github.cdimascio" % "dotenv-java" % DotEnvJavaVersion,
      "com.chatwork" %% "scala-ulid" % ULIDVersion,
      "org.jxls" % "jxls" % jXlsVersion,
      "org.jxls" % "jxls-poi" % jXlsVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "ch.qos.logback" % "logback-core" % LogbackVersion,
    ),
    assembly / assemblyJarName := "open-reports-api.jar",
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
