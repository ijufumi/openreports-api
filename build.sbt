val Scala213Version = "2.13.16"
val Scala212Version = "2.12.20"

// for scala libraries
val ScalatraVersion = "3.1.2"
val SlickVersion = "3.6.1"
val ScalaCacheVersion = "0.28.0"
val SttpVersion = "4.0.9"
val Json4sVersion = "4.0.7"
val ULIDVersion = "1.0.24"

// for java libraries
val SLF4JVersion = "2.0.17"
val LogbackVersion = "1.5.18"
val FlywayVersion = "9.22.3"
val AWSSDKVersion = "2.31.78"
val jXlsVersion = "3.0.0"
val Auth0Version = "2.22.0"
val GuiceVersion = "7.0.0"
val GuiceExtensionVersion = "7.0.0"
val DotEnvJavaVersion = "3.2.0"
val JettyVersion = "12.0.23"
val PostgresVersion = "42.7.7"
val ServletAPIVersion = "6.1.0"

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
      "org.scalatra" %% "scalatra-jakarta" % ScalatraVersion,
      "org.scalatra" %% "scalatra-json-jakarta" % ScalatraVersion,
      "org.scalatra" %% "scalatra-forms-jakarta" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest-jakarta" % ScalatraVersion % "test",
      "org.scalatestplus" %% "mockito-4-6" % "3.2.14.0" % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion % "runtime",
      "org.slf4j" % "slf4j-api" % SLF4JVersion,
      "org.slf4j" % "jcl-over-slf4j" % SLF4JVersion,
      "org.eclipse.jetty.ee10" % "jetty-ee10-webapp" % JettyVersion % "container;compile",
      "jakarta.servlet" % "jakarta.servlet-api" % ServletAPIVersion % "provided",
      "org.postgresql" % "postgresql" % PostgresVersion,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
      "com.google.inject" % "guice" % GuiceVersion,
      "com.google.inject.extensions" % "guice-servlet" % GuiceExtensionVersion,
      "org.json4s" %% "json4s-jackson" % Json4sVersion,
      "org.json4s" %% "json4s-native" % Json4sVersion,
      "org.json4s" %% "json4s-ext" % Json4sVersion,
      "commons-codec" % "commons-codec" % "1.15",
      "com.auth0" % "auth0" % Auth0Version,
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
      "com.typesafe.slick" %% "slick-testkit" % SlickVersion % "test",
      "com.github.cb372" %% "scalacache-core" % ScalaCacheVersion exclude ("org.slf4j", "slf4j-api"),
      "com.github.cb372" %% "scalacache-redis" % ScalaCacheVersion exclude ("org.slf4j", "slf4j-api"),
      "com.softwaremill.sttp.client4" %% "core" % SttpVersion,
      "com.softwaremill.sttp.client4" %% "json4s" % SttpVersion,
      "software.amazon.awssdk" % "s3" % AWSSDKVersion,
      "io.github.cdimascio" % "dotenv-java" % DotEnvJavaVersion,
      "com.chatwork" %% "scala-ulid" % ULIDVersion,
      "org.jxls" % "jxls" % jXlsVersion,
      "org.jxls" % "jxls-poi" % jXlsVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "ch.qos.logback" % "logback-core" % LogbackVersion,
      "com.lihaoyi" %% "os-lib" % "0.11.4",
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
