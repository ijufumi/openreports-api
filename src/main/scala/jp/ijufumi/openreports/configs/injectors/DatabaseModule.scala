package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.gateways.datastores.database.db.DatabaseFactory
import slick.jdbc.JdbcBackend.Database

class DatabaseModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindProvider(classOf[Database], () => DatabaseFactory.createDatabase())
  }
}
