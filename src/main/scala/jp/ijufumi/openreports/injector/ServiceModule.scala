package jp.ijufumi.openreports.injector

import jp.ijufumi.openreports.services._
import jp.ijufumi.openreports.services.impl._

class ServiceModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindAsSingleton(classOf[LoginService], classOf[LoginServiceImpl])
    bindAsSingleton(classOf[ReportService], classOf[ReportServiceImpl])
    bindAsSingleton(classOf[OutputService], classOf[OutputServiceImpl])
    bindAsSingleton(classOf[DataSourceService], classOf[DataSourceServiceImpl])
    bindAsSingleton(classOf[StorageService], classOf[StorageServiceImpl])
  }
}
