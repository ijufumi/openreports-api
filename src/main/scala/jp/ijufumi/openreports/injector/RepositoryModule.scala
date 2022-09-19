package jp.ijufumi.openreports.injector

import com.google.inject.{AbstractModule, Singleton}
import jp.ijufumi.openreports.repositories.system._
import jp.ijufumi.openreports.repositories.system.impl._
import jp.ijufumi.openreports.repositories.db._
import jp.ijufumi.openreports.repositories.db.impl._

class RepositoryModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[MemberRepository])
      .to(classOf[MemberRepositoryImpl])
      .in(classOf[Singleton])
    bind(classOf[GoogleRepository])
      .to(classOf[GoogleRepositoryImpl])
      .in(classOf[Singleton])
    bind(classOf[WorkspaceRepository])
      .to(classOf[WorkspaceRepositoryImpl])
      .in(classOf[Singleton])
    bind(classOf[WorkspaceMemberRepository])
      .to(classOf[WorkspaceMemberRepositoryImpl])
      .in(classOf[Singleton])
    bind(classOf[DataSourceRepository])
      .to(classOf[DataSourceRepositoryImpl])
      .in(classOf[Singleton])
    bind(classOf[ReportRepository])
      .to(classOf[ReportRepositoryImpl])
      .in(classOf[Singleton])
    bind(classOf[ReportTemplateRepository])
      .to(classOf[ReportTemplateRepositoryImpl])
      .in(classOf[Singleton])
  }
}
