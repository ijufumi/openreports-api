package jp.ijufumi.openreports.injector

import jp.ijufumi.openreports.cache.CacheWrapper

class CacheModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindInstance(classOf[CacheWrapper], new CacheWrapper())
  }
}
