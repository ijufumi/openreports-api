package jp.ijufumi.openreports.injector

import com.google.inject.AbstractModule
import jp.ijufumi.openreports.database.DatabaseFactory
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

class DatabaseModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[Database])
      .toInstance(DatabaseFactory.createDatabase())
    super.configure()
    bind(classOf[DatabaseConfig[JdbcProfile]])
      .toInstance(DatabaseFactory.createDatabaseConfig())
  }
}
