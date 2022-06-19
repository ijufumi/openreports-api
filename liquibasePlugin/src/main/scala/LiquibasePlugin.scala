import sbt._
import liquibase.integration.commandline.LiquibaseCommandLine

object Import {
  // settings
  val liquibaseChangeLogFile = SettingKey[String]("Specifies the root changelog")
  val liquibaseDatabaseClass = SettingKey[String]("Specifies the JDBC driver class")
  val liquibaseUrl = SettingKey[String]("Specifies the JDBC database connection URL")
  val liquibaseUsername = SettingKey[String]("Specifies the database username")
  val liquibasePassword = SettingKey[String]("Specifies the database password")
  // tasks
  val liquibaseUpdate = TaskKey[Unit]("liquibase-update", "Run a liquibase migration")
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

      LiquibaseCommandLine.main(parameters)
    }
  )
}
