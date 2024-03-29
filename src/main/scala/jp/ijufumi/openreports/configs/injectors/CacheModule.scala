package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.infrastructure.datastores.cache.CacheWrapper

class CacheModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindInstance(classOf[CacheWrapper], new CacheWrapper())
  }
}
