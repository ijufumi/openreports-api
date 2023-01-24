package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.services.impl.{
  DataSourceServiceImpl,
  DriverTypeServiceImpl,
  LoginServiceImpl,
  OutputServiceImpl,
  ReportServiceImpl,
  RoleServiceImpl,
  StorageServiceImpl,
  WorkspaceServiceImpl,
}
import jp.ijufumi.openreports.services.{
  DataSourceService,
  DriverTypeService,
  LoginService,
  OutputService,
  ReportService,
  RoleService,
  StorageService,
  WorkspaceService,
}

class ServiceModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindAsSingleton(classOf[LoginService], classOf[LoginServiceImpl])
    bindAsSingleton(classOf[ReportService], classOf[ReportServiceImpl])
    bindAsSingleton(classOf[OutputService], classOf[OutputServiceImpl])
    bindAsSingleton(classOf[DataSourceService], classOf[DataSourceServiceImpl])
    bindAsSingleton(classOf[StorageService], classOf[StorageServiceImpl])
    bindAsSingleton(classOf[WorkspaceService], classOf[WorkspaceServiceImpl])
    bindAsSingleton(classOf[RoleService], classOf[RoleServiceImpl])
    bindAsSingleton(classOf[DriverTypeService], classOf[DriverTypeServiceImpl])
  }
}
