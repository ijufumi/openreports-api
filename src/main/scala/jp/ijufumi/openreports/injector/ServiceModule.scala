package jp.ijufumi.openreports.injector

import com.google.inject.{AbstractModule, Singleton}
import jp.ijufumi.openreports.services._
import jp.ijufumi.openreports.services.impl._

class ServiceModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[LoginService])
      .to(classOf[LoginServiceImpl])
      .in(classOf[Singleton])
    bind(classOf[ReportService])
      .to(classOf[ReportServiceImpl])
      .in(classOf[Singleton])
    bind(classOf[OutputService])
      .to(classOf[OutputServiceImpl])
      .in(classOf[Singleton])
    bind(classOf[DataSourceService])
      .to(classOf[DataSourceServiceImpl])
      .in(classOf[Singleton])
  }
}
