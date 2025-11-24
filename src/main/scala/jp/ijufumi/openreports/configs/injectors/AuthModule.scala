package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.infrastructure.external.google.GoogleRepository
import jp.ijufumi.openreports.infrastructure.external.google.impl.GoogleRepositoryImpl

class AuthModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindClass(classOf[GoogleRepository], classOf[GoogleRepositoryImpl])
  }
}
