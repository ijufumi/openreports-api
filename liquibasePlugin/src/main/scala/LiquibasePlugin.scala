import sbt._
import liquibase.integration.commandline.LiquibaseCommandLine

object Import {
  // settings
  val liquibaseChangeLogFile = SettingKey[String]("Specifies the root changelog")
  val liquibaseUrl = SettingKey[String]("Specifies the JDBC database connection URL")
  val liquibaseUsername = SettingKey[String]("Specifies the database username")
  val liquibasePassword = SettingKey[String]("Specifies the database password")
  // tasks
  val liquibaseUpdate = TaskKey[Unit]("liquibase-update", "Run a liquibase migration")
}

object LiquibasePlugin extends AutoPlugin {
  import Import._

}
