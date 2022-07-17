package jp.ijufumi.openreports.injector

import com.google.inject.AbstractModule
import jp.ijufumi.openreports.cache.CacheWrapper

class CacheModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[CacheWrapper])
      .toInstance(new CacheWrapper())
  }
}
