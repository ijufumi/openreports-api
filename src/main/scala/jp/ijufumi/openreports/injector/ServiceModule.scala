package jp.ijufumi.openreports.injector

import com.google.inject.AbstractModule
import jp.ijufumi.openreports.services._
import jp.ijufumi.openreports.services.impl._

class ServiceModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[LoginService])
      .to(classOf[LoginServiceImpl])
    bind(classOf[GoogleLoginService])
      .to(classOf[GoogleLoginServiceImpl])
  }
}
