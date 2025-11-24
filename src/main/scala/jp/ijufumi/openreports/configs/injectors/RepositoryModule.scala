package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.domain.repository.{DataSourceRepository, DriverTypeRepository, FunctionRepository, MemberRepository, ReportGroupReportRepository, ReportGroupRepository, ReportParameterRepository, ReportReportParameterRepository, ReportRepository, ReportTemplateRepository, RoleFunctionRepository, RoleRepository, StorageS3Repository, WorkspaceMemberRepository, WorkspaceRepository}
import jp.ijufumi.openreports.infrastructure.persistence.repository.{DataSourceRepositoryImpl, DriverTypeRepositoryImpl, FunctionRepositoryImpl, MemberRepositoryImpl, ReportGroupReportRepositoryImpl, ReportGroupRepositoryImpl, ReportParameterRepositoryImpl, ReportReportParameterRepositoryImpl, ReportRepositoryImpl, ReportTemplateRepositoryImpl, RoleFunctionRepositoryImpl, RoleRepositoryImpl, StorageS3RepositoryImpl, WorkspaceMemberRepositoryImpl, WorkspaceRepositoryImpl}

class RepositoryModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindClass(classOf[MemberRepository], classOf[MemberRepositoryImpl])
    bindClass(classOf[WorkspaceRepository], classOf[WorkspaceRepositoryImpl])
    bindClass(classOf[WorkspaceMemberRepository], classOf[WorkspaceMemberRepositoryImpl])
    bindClass(classOf[DataSourceRepository], classOf[DataSourceRepositoryImpl])
    bindClass(classOf[ReportRepository], classOf[ReportRepositoryImpl])
    bindClass(classOf[ReportGroupRepository], classOf[ReportGroupRepositoryImpl])
    bindClass(classOf[ReportGroupReportRepository], classOf[ReportGroupReportRepositoryImpl])
    bindClass(classOf[ReportParameterRepository], classOf[ReportParameterRepositoryImpl])
    bindClass(
      classOf[ReportReportParameterRepository],
      classOf[ReportReportParameterRepositoryImpl],
    )
    bindClass(classOf[ReportTemplateRepository], classOf[ReportTemplateRepositoryImpl])
    bindClass(classOf[StorageS3Repository], classOf[StorageS3RepositoryImpl])
    bindClass(classOf[RoleRepository], classOf[RoleRepositoryImpl])
    bindClass(classOf[FunctionRepository], classOf[FunctionRepositoryImpl])
    bindClass(classOf[RoleFunctionRepository], classOf[RoleFunctionRepositoryImpl])
    bindClass(classOf[DriverTypeRepository], classOf[DriverTypeRepositoryImpl])
  }
}
