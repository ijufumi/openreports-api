package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.domain.port.{
  AppConfigPort,
  CachePort,
  ConnectionPoolPort,
  GoogleAuthPort,
}
import jp.ijufumi.openreports.infrastructure.cache.CacheAdapter
import jp.ijufumi.openreports.infrastructure.config.AppConfigAdapter
import jp.ijufumi.openreports.infrastructure.external.google.GoogleAuthAdapter
import jp.ijufumi.openreports.infrastructure.persistence.database.pool.ConnectionPoolAdapter

class PortModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindClass(classOf[GoogleAuthPort], classOf[GoogleAuthAdapter])
    bindClass(classOf[CachePort], classOf[CacheAdapter])
    bindClass(classOf[AppConfigPort], classOf[AppConfigAdapter])
    bindClass(classOf[ConnectionPoolPort], classOf[ConnectionPoolAdapter])
  }
}
