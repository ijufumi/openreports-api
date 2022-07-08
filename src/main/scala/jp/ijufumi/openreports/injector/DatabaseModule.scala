package jp.ijufumi.openreports.injector

import com.google.inject.AbstractModule
import jp.ijufumi.openreports.database.DatabaseFactory
import slick.jdbc.JdbcBackend.Database

class DatabaseModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[Database])
      .toInstance(DatabaseFactory.createDatabase())
  }
}
