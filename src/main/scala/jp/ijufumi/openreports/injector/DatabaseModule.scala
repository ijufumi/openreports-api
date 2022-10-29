package jp.ijufumi.openreports.injector

import jp.ijufumi.openreports.database.DatabaseFactory
import slick.jdbc.JdbcBackend.Database

class DatabaseModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindInstance(classOf[Database], DatabaseFactory.createDatabase())
  }
}
