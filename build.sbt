val Scala3Version = "3.3.7"

// for scala libraries
val ScalatraVersion = "3.1.2"
val SlickVersion = "3.6.1"
val SttpVersion = "4.0.9"
val Json4sVersion = "4.1.0-M8"
val JedisVersion = "5.2.0"

// for java libraries
val SLF4JVersion = "2.0.17"
val LogbackVersion = "1.5.18"
val FlywayVersion = "11.11.0"
val AWSSDKVersion = "2.31.78"
val jXlsVersion = "3.0.0"
val Auth0Version = "2.22.0"
val GuiceVersion = "7.0.0"
val GuiceExtensionVersion = "7.0.0"
val DotEnvJavaVersion = "3.2.0"
val JettyVersion = "12.0.23"
val PostgresVersion = "42.7.7"
val ServletAPIVersion = "6.1.0"
val H2Version = "2.3.232"

ThisBuild / scalaVersion := Scala3Version
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
      "org.scalamock" %% "scalamock" % "7.3.1" % "test",
      "org.scalatestplus" %% "mockito-5-12" % "3.2.19.0" % "test",
      "com.h2database" % "h2" % H2Version % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion % "runtime",
      "org.slf4j" % "slf4j-api" % SLF4JVersion,
      "org.slf4j" % "jcl-over-slf4j" % SLF4JVersion,
      "org.eclipse.jetty.ee10" % "jetty-ee10-webapp" % JettyVersion % "container;compile",
      "jakarta.servlet" % "jakarta.servlet-api" % ServletAPIVersion % "provided",
      "org.postgresql" % "postgresql" % PostgresVersion,
      "org.flywaydb" % "flyway-core" % FlywayVersion,
      "org.flywaydb" % "flyway-database-postgresql" % FlywayVersion,
      "com.google.inject" % "guice" % GuiceVersion,
      "com.google.inject.extensions" % "guice-servlet" % GuiceExtensionVersion,
      "org.json4s" %% "json4s-jackson" % Json4sVersion,
      "org.json4s" %% "json4s-native" % Json4sVersion,
      "org.json4s" %% "json4s-ext" % Json4sVersion,
      "org.scala-lang" %% "scala3-staging" % Scala3Version,
      "commons-codec" % "commons-codec" % "1.15",
      "com.auth0" % "auth0" % Auth0Version,
      "com.typesafe.slick" %% "slick" % SlickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
      "com.typesafe.slick" %% "slick-testkit" % SlickVersion % "test",
      "redis.clients" % "jedis" % JedisVersion,
      "com.softwaremill.sttp.client4" %% "core" % SttpVersion,
      "com.softwaremill.sttp.client4" %% "json4s" % SttpVersion,
      "software.amazon.awssdk" % "s3" % AWSSDKVersion,
      "io.github.cdimascio" % "dotenv-java" % DotEnvJavaVersion,
      "com.github.f4b6a3" % "ulid-creator" % "5.2.3",
      "org.jxls" % "jxls" % jXlsVersion,
      "org.jxls" % "jxls-poi" % jXlsVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "ch.qos.logback" % "logback-core" % LogbackVersion,
      "com.lihaoyi" %% "os-lib" % "0.11.4",
    ),
    assembly / assemblyJarName := "open-reports-api.jar",
    assembly / mainClass := Some("JettyLauncher"),
    Test / javaOptions += "-Dnet.bytebuddy.experimental=true",
    Test / fork := true,
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

// Test coverage settings
coverageEnabled := false // Enable with 'sbt coverage test'
coverageMinimumStmtTotal := 60
coverageFailOnMinimum := false // Set to true once we reach target
coverageHighlighting := true
coverageExcludedPackages := ".*JettyLauncher.*;.*configs.injectors.*"
