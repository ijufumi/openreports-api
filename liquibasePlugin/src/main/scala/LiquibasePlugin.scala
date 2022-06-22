import sbt._
import liquibase.integration.commandline.LiquibaseCommandLine
import liquibase.Liquibase
import liquibase.integration.commandline.CommandLineUtils
import liquibase.resource.{ClassLoaderResourceAccessor, FileSystemResourceAccessor}

object Import {
  // settings
  val liquibaseChangeLogFile = settingKey[String]("Specifies the root changelog")
  val liquibaseDatabaseClass = settingKey[String]("Specifies the JDBC driver class")
  val liquibaseUrl = settingKey[String]("Specifies the JDBC database connection URL")
  val liquibaseUsername = settingKey[String]("Specifies the database username")
  val liquibasePassword = settingKey[String]("Specifies the database password")
  // tasks
  val liquibaseUpdate = taskKey[Unit]("Run a liquibase migration")

  lazy val liquibaseInstance = taskKey[Liquibase]("liquibaseInstance")
}

object LiquibasePlugin extends AutoPlugin {
  import Import._

  override lazy val globalSettings: Seq[Setting[_]] = Seq(
    liquibaseChangeLogFile := "src/main/resources/migration",
    liquibaseUsername := "root",
    liquibasePassword := "password",
    liquibaseDatabaseClass := "org.postgresql.Driver",
    liquibaseUrl := ""
  )

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    liquibaseInstance := { () =>
      val classpath = (dependencyClasspath in conf).value
      val accessor =
        new ClassLoaderResourceAccessor(ClasspathUtilities.toLoader(classpath.map(_.data)))
      val database = CommandLineUtils.createDatabaseObject(
        accessor,
        liquibaseUrl.value,
        liquibaseUsername.value,
        liquibasePassword.value,
        liquibaseDatabaseClass.value,
        liquibaseDefaultCatalog.value.orNull,
        liquibaseDefaultSchemaName.value.orNull,
        false, // outputDefaultCatalog
        true, // outputDefaultSchema
        null, // databaseClass
        null, // driverPropertiesFile
        null, // propertyProviderClass
        liquibaseChangelogCatalog.value.orNull,
        liquibaseChangelogSchemaName.value.orNull,
        null, // databaseChangeLogTableName
        null // databaseChangeLogLockTableName
      )
      new Liquibase(liquibaseChangelog.value.absolutePath, new FileSystemResourceAccessor, database)
    },
    liquibaseUpdate := {
      val parameters = Array[String]("update")
      parameters ++ "--changelog-file"
      parameters ++ liquibaseChangeLogFile.value
      parameters ++ "--url"
      parameters ++ liquibaseUrl.value
      parameters ++ "--username"
      parameters ++ liquibaseUsername.value
      parameters ++ "--password"
      parameters ++ liquibasePassword.value

      new LiquibaseCommandLine().execute(parameters)
    }
  )
}

https://github.com/sbtliquibase/sbt-liquibase-plugin/blob/master/src/main/scala/com/github/sbtliquibase/SbtLiquibase.scala
