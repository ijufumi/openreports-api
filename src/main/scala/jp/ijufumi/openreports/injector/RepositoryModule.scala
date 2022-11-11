package jp.ijufumi.openreports.injector

import jp.ijufumi.openreports.repositories.system._
import jp.ijufumi.openreports.repositories.system.impl._
import jp.ijufumi.openreports.repositories.db._
import jp.ijufumi.openreports.repositories.db.impl._
import jp.ijufumi.openreports.repositories.services.GoogleRepository
import jp.ijufumi.openreports.repositories.services.impl.GoogleRepositoryImpl

class RepositoryModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    // db
    bindAsSingleton(classOf[MemberRepository], classOf[MemberRepositoryImpl])
    bindAsSingleton(classOf[WorkspaceRepository], classOf[WorkspaceRepositoryImpl])
    bindAsSingleton(classOf[WorkspaceMemberRepository], classOf[WorkspaceMemberRepositoryImpl])
    bindAsSingleton(classOf[DataSourceRepository], classOf[DataSourceRepositoryImpl])
    bindAsSingleton(classOf[ReportRepository], classOf[ReportRepositoryImpl])
    bindAsSingleton(classOf[ReportTemplateRepository], classOf[ReportTemplateRepositoryImpl])
    bindAsSingleton(classOf[StorageRepository], classOf[StorageRepositoryImpl])
    // system
    bindAsSingleton(classOf[GoogleRepository], classOf[GoogleRepositoryImpl])
    bindAsSingleton(classOf[LocalFileRepository], classOf[LocalFileRepositoryImpl])
    bindAsSingleton(classOf[AwsS3Repository], classOf[AwsS3RepositoryImpl])
  }
}
