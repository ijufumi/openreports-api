import sbt.*
import sbt.Keys.*

import liquibase.Liquibase
import liquibase.integration.commandline.CommandLineUtils
import liquibase.resource.{ClassLoaderResourceAccessor, FileSystemResourceAccessor}

object Import {
  // settings
  val liquibaseChangeLogFile = settingKey[File]("Specifies the root changelog")
  val liquibaseDatabaseClass = settingKey[String]("Specifies the JDBC driver class")
  val liquibaseUrl = settingKey[String]("Specifies the JDBC database connection URL")
  val liquibaseUsername = settingKey[String]("Specifies the database username")
  val liquibasePassword = settingKey[String]("Specifies the database password")
  val liquibaseDefaultCatalog = settingKey[String]("")
  val liquibaseDefaultSchemaName = settingKey[String]("")
  val liquibaseChangelogCatalog = settingKey[String]("")
  val liquibaseChangelogSchemaName = settingKey[String]("")
  val liquibaseChangelog = settingKey[File]("")
  val liquibaseContext = settingKey[String]("changeSet contexts to execute")
  // tasks
  val liquibaseUpdate = taskKey[Unit]("Run a liquibase migration")

  lazy val liquibaseInstance = taskKey[(Keys.Classpath) => Liquibase]("liquibaseInstance")

  // global settings
  val compileClassPath = taskKey[Keys.Classpath]("")
}

object LiquibasePlugin extends AutoPlugin {
  import Import.*

  def toLoader(cp: Types.Id[Keys.Classpath]): ClassLoader = {
    // TODO: Should change it to alternative ways because of internal class.
    val classloader = sbt.internal.inc.classpath.ClasspathUtilities
      .toLoader(cp.map(_.data), getClass.getClassLoader)

    java.lang.Thread.currentThread.setContextClassLoader(classloader)
    classloader
  }

  implicit class LiquibaseWrapper(val liquibase: Liquibase) extends AnyVal {
    def execAndClose(f: Liquibase => Unit): Unit = {
      try { f(liquibase) } finally { liquibase.getDatabase.close() }
    }
  }

  def liquibaseBaseSettings(): Seq[Setting[_]] = {
    Seq[Setting[_]](
      compileClassPath := (fullClasspath in Compile).value,
      liquibaseChangeLogFile := file("src/main/resources/migration/changelog.xml"),
      liquibaseUsername := "root",
      liquibasePassword := "password",
      liquibaseDatabaseClass := "org.postgresql.Driver",
      liquibaseUrl := "jdbc:postgresql:database",
      liquibaseDefaultSchemaName := "",
      liquibaseChangelogCatalog := "",
      liquibaseChangelogSchemaName := "",
      liquibaseDefaultCatalog := "",
      liquibaseContext := "",
      liquibaseInstance := { (cp: Types.Id[Keys.Classpath]) =>
        val accessor =
          new ClassLoaderResourceAccessor(toLoader(cp))
        val database = CommandLineUtils.createDatabaseObject(
          accessor,
          liquibaseUrl.value,
          liquibaseUsername.value,
          liquibasePassword.value,
          liquibaseDatabaseClass.value,
          liquibaseDefaultCatalog.value,
          liquibaseDefaultSchemaName.value,
          true, // outputDefaultCatalog
          true, // outputDefaultSchema
          null, // databaseClass
          null, // driverPropertiesFile
          null, // propertyProviderClass
          liquibaseChangelogCatalog.value,
          liquibaseChangelogSchemaName.value,
          null, // databaseChangeLogTableName
          null // databaseChangeLogLockTableName
        )
        new Liquibase(liquibaseChangeLogFile.value.absolutePath,
                      new FileSystemResourceAccessor,
                      database)
      },
      liquibaseUpdate := liquibaseInstance
        .value(compileClassPath.value)
        .execAndClose(_.update(liquibaseContext.value))
    )
  }

  override lazy val projectSettings: Seq[Setting[_]] = liquibaseBaseSettings()
}

// https://github.com/sbtliquibase/sbt-liquibase-plugin/blob/master/src/main/scala/com/github/sbtliquibase/SbtLiquibase.scala
