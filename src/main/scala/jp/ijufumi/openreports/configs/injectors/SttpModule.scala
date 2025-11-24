package jp.ijufumi.openreports.configs.injectors

import sttp.client4.WebSocketSyncBackend
import sttp.client4.httpclient.HttpClientSyncBackend

class SttpModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindInstance(classOf[WebSocketSyncBackend], HttpClientSyncBackend())
  }
}
