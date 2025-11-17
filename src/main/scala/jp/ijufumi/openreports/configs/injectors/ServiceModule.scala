package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.usecase.interactor.{
  DataSourceInteractor,
  DriverTypeInteractor,
  LoginInteractor,
  MemberInteractor,
  OutputInteractor,
  ReportInteractor,
  RoleInteractor,
  StorageInteractor,
  WorkspaceInteractor,
}
import jp.ijufumi.openreports.usecase.port.input.{
  DataSourceUseCase,
  DriverTypeUseCase,
  LoginUseCase,
  MemberUseCase,
  OutputUseCase,
  ReportUseCase,
  RoleUseCase,
  StorageUseCase,
  WorkspaceUseCase,
}

class ServiceModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindClass(classOf[LoginUseCase], classOf[LoginInteractor])
    bindClass(classOf[MemberUseCase], classOf[MemberInteractor])
    bindClass(classOf[ReportUseCase], classOf[ReportInteractor])
    bindClass(classOf[OutputUseCase], classOf[OutputInteractor])
    bindClass(classOf[DataSourceUseCase], classOf[DataSourceInteractor])
    bindClass(classOf[StorageUseCase], classOf[StorageInteractor])
    bindClass(classOf[WorkspaceUseCase], classOf[WorkspaceInteractor])
    bindClass(classOf[RoleUseCase], classOf[RoleInteractor])
    bindClass(classOf[DriverTypeUseCase], classOf[DriverTypeInteractor])
  }
}
