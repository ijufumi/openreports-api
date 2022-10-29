package jp.ijufumi.openreports.injector

import jp.ijufumi.openreports.repositories.system._
import jp.ijufumi.openreports.repositories.system.impl._
import jp.ijufumi.openreports.repositories.db._
import jp.ijufumi.openreports.repositories.db.impl._

class RepositoryModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindAsSingleton(classOf[MemberRepository], classOf[MemberRepositoryImpl])
    bindAsSingleton(classOf[GoogleRepository], classOf[GoogleRepositoryImpl])
    bindAsSingleton(classOf[WorkspaceRepository], classOf[WorkspaceRepositoryImpl])
    bindAsSingleton(classOf[WorkspaceMemberRepository], classOf[WorkspaceMemberRepositoryImpl])
    bindAsSingleton(classOf[DataSourceRepository], classOf[DataSourceRepositoryImpl])
    bindAsSingleton(classOf[ReportRepository], classOf[ReportRepositoryImpl])
    bindAsSingleton(classOf[ReportTemplateRepository], classOf[ReportTemplateRepositoryImpl])
    bindAsSingleton(classOf[StorageRepository], classOf[StorageRepositoryImpl])
  }
}
