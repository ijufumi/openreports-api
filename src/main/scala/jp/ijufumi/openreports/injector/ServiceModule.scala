package jp.ijufumi.openreports.injector

import com.google.inject.AbstractModule
import jp.ijufumi.openreports.services._
import jp.ijufumi.openreports.services.impl._

class ServiceModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[LoginService])
      .to(classOf[LoginServiceImpl])
    bind(classOf[ReportService])
      .to(classOf[ReportServiceImpl])
    bind(classOf[OutputService])
      .to(classOf[OutputServiceImpl])
    bind(classOf[DataSourceService])
      .to(classOf[DataSourceServiceImpl])
  }
}
