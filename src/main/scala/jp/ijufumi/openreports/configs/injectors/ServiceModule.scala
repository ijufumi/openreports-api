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
    bindClass(classOf[LoginService], classOf[LoginServiceImpl])
    bindClass(classOf[MemberService], classOf[MemberServiceImpl])
    bindClass(classOf[ReportService], classOf[ReportServiceImpl])
    bindClass(classOf[OutputService], classOf[OutputServiceImpl])
    bindClass(classOf[DataSourceService], classOf[DataSourceServiceImpl])
    bindClass(classOf[StorageService], classOf[StorageServiceImpl])
    bindClass(classOf[WorkspaceService], classOf[WorkspaceServiceImpl])
    bindClass(classOf[RoleService], classOf[RoleServiceImpl])
    bindClass(classOf[DriverTypeService], classOf[DriverTypeServiceImpl])
  }
}
