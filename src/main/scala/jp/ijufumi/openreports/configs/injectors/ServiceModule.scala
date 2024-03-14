package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.services.impl.{
  DataSourceServiceImpl,
  DriverTypeServiceImpl,
  LoginServiceImpl,
  MemberServiceImpl,
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
  MemberService,
  OutputService,
  ReportService,
  RoleService,
  StorageService,
  WorkspaceService,
}

class ServiceModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[LoginService], classOf[LoginServiceImpl])
    bind(classOf[MemberService], classOf[MemberServiceImpl])
    bind(classOf[ReportService], classOf[ReportServiceImpl])
    bind(classOf[OutputService], classOf[OutputServiceImpl])
    bind(classOf[DataSourceService], classOf[DataSourceServiceImpl])
    bind(classOf[StorageService], classOf[StorageServiceImpl])
    bind(classOf[WorkspaceService], classOf[WorkspaceServiceImpl])
    bind(classOf[RoleService], classOf[RoleServiceImpl])
    bind(classOf[DriverTypeService], classOf[DriverTypeServiceImpl])
  }
}
