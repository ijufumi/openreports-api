package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.gateways.auth.google.GoogleRepository
import jp.ijufumi.openreports.gateways.datastores.database.repositories.{DataSourceRepository, MemberRepository, ReportRepository, StorageRepository, TemplateRepository, WorkspaceMemberRepository, WorkspaceRepository}
import jp.ijufumi.openreports.gateways.datastores.database.repositories.impl.{MemberRepositoryImpl, WorkspaceRepositoryImpl, WorkspaceMemberRepositoryImpl, DataSourceRepositoryImpl, ReportRepositoryImpl, TemplateRepositoryImpl, StorageRepositoryImpl}
import jp.ijufumi.openreports.gateways.auth.google.impl.GoogleRepositoryImpl
import jp.ijufumi.openreports.gateways.filestores.local.LocalFileRepository
import jp.ijufumi.openreports.gateways.filestores.local.impl.LocalFileRepositoryImpl
import jp.ijufumi.openreports.gateways.filestores.s3.AwsS3Repository
import jp.ijufumi.openreports.gateways.filestores.s3.impl.AwsS3RepositoryImpl

class RepositoryModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    // db
    bindAsSingleton(classOf[MemberRepository], classOf[MemberRepositoryImpl])
    bindAsSingleton(classOf[WorkspaceRepository], classOf[WorkspaceRepositoryImpl])
    bindAsSingleton(classOf[WorkspaceMemberRepository], classOf[WorkspaceMemberRepositoryImpl])
    bindAsSingleton(classOf[DataSourceRepository], classOf[DataSourceRepositoryImpl])
    bindAsSingleton(classOf[ReportRepository], classOf[ReportRepositoryImpl])
    bindAsSingleton(classOf[TemplateRepository], classOf[TemplateRepositoryImpl])
    bindAsSingleton(classOf[StorageRepository], classOf[StorageRepositoryImpl])
    // auth
    bindAsSingleton(classOf[GoogleRepository], classOf[GoogleRepositoryImpl])
    // file store
    bindAsSingleton(classOf[LocalFileRepository], classOf[LocalFileRepositoryImpl])
    bindAsSingleton(classOf[AwsS3Repository], classOf[AwsS3RepositoryImpl])
  }
}
