package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.{
  DataSourceRepository,
  DriverTypeRepository,
  FunctionRepository,
  MemberRepository,
  ReportGroupReportRepository,
  ReportGroupRepository,
  ReportParameterRepository,
  ReportReportParameterRepository,
  ReportRepository,
  RoleFunctionRepository,
  RoleRepository,
  StorageS3Repository,
  TemplateRepository,
  WorkspaceMemberRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl.{
  DataSourceRepositoryImpl,
  DriverTypeRepositoryImpl,
  FunctionRepositoryImpl,
  MemberRepositoryImpl,
  ReportGroupReportRepositoryImpl,
  ReportGroupRepositoryImpl,
  ReportParameterRepositoryImpl,
  ReportReportParameterRepositoryImpl,
  ReportRepositoryImpl,
  RoleFunctionRepositoryImpl,
  RoleRepositoryImpl,
  StorageS3RepositoryImpl,
  TemplateRepositoryImpl,
  WorkspaceMemberRepositoryImpl,
  WorkspaceRepositoryImpl,
}
import jp.ijufumi.openreports.infrastructure.filestores.local.LocalFileRepository
import jp.ijufumi.openreports.infrastructure.filestores.local.impl.LocalFileRepositoryImpl
import jp.ijufumi.openreports.infrastructure.filestores.s3.AwsS3Repository
import jp.ijufumi.openreports.infrastructure.filestores.s3.impl.AwsS3RepositoryImpl
import jp.ijufumi.openreports.infrastructure.google.auth.GoogleRepository
import jp.ijufumi.openreports.infrastructure.google.auth.impl.GoogleRepositoryImpl

class RepositoryModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    // db
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
    bindClass(classOf[TemplateRepository], classOf[TemplateRepositoryImpl])
    bindClass(classOf[StorageS3Repository], classOf[StorageS3RepositoryImpl])
    bindClass(classOf[RoleRepository], classOf[RoleRepositoryImpl])
    bindClass(classOf[FunctionRepository], classOf[FunctionRepositoryImpl])
    bindClass(classOf[RoleFunctionRepository], classOf[RoleFunctionRepositoryImpl])
    bindClass(classOf[DriverTypeRepository], classOf[DriverTypeRepositoryImpl])
    // auth
    bindClass(classOf[GoogleRepository], classOf[GoogleRepositoryImpl])
    // file store
    bindClass(classOf[LocalFileRepository], classOf[LocalFileRepositoryImpl])
    bindClass(classOf[AwsS3Repository], classOf[AwsS3RepositoryImpl])
  }
}
