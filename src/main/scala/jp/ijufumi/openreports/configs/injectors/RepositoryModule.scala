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
    bind(classOf[MemberRepository], classOf[MemberRepositoryImpl])
    bind(classOf[WorkspaceRepository], classOf[WorkspaceRepositoryImpl])
    bind(classOf[WorkspaceMemberRepository], classOf[WorkspaceMemberRepositoryImpl])
    bind(classOf[DataSourceRepository], classOf[DataSourceRepositoryImpl])
    bind(classOf[ReportRepository], classOf[ReportRepositoryImpl])
    bind(classOf[ReportGroupRepository], classOf[ReportGroupRepositoryImpl])
    bind(classOf[ReportGroupReportRepository], classOf[ReportGroupReportRepositoryImpl])
    bind(classOf[ReportParameterRepository], classOf[ReportParameterRepositoryImpl])
    bind(
      classOf[ReportReportParameterRepository],
      classOf[ReportReportParameterRepositoryImpl],
    )
    bind(classOf[TemplateRepository], classOf[TemplateRepositoryImpl])
    bind(classOf[StorageS3Repository], classOf[StorageS3RepositoryImpl])
    bind(classOf[RoleRepository], classOf[RoleRepositoryImpl])
    bind(classOf[FunctionRepository], classOf[FunctionRepositoryImpl])
    bind(classOf[RoleFunctionRepository], classOf[RoleFunctionRepositoryImpl])
    bind(classOf[DriverTypeRepository], classOf[DriverTypeRepositoryImpl])
    // auth
    bind(classOf[GoogleRepository], classOf[GoogleRepositoryImpl])
    // file store
    bind(classOf[LocalFileRepository], classOf[LocalFileRepositoryImpl])
    bind(classOf[AwsS3Repository], classOf[AwsS3RepositoryImpl])
  }
}
