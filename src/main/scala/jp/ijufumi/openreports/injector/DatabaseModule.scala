package jp.ijufumi.openreports.injector

import com.google.inject.AbstractModule
import jp.ijufumi.openreports.repositories.database.Config
import slick.jdbc.JdbcBackend.Database

class DatabaseModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[Database])
      .toInstance(Config.createDatabase())
  }
}
