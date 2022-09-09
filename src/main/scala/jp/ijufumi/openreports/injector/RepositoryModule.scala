package jp.ijufumi.openreports.injector

import com.google.inject.AbstractModule
import jp.ijufumi.openreports.repositories.system._
import jp.ijufumi.openreports.repositories.system.impl._
import jp.ijufumi.openreports.repositories.db._
import jp.ijufumi.openreports.repositories.db.impl._

class RepositoryModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[MemberRepository])
      .to(classOf[MemberRepositoryImpl])
    bind(classOf[GoogleRepository])
      .to(classOf[GoogleRepositoryImpl])
    bind(classOf[WorkspaceRepository])
      .to(classOf[WorkspaceRepositoryImpl])
    bind(classOf[WorkspaceMemberRepository])
      .to(classOf[WorkspaceMemberRepositoryImpl])
    bind(classOf[DataSourceRepository])
      .to(classOf[DataSourceRepositoryImpl])
  }
}
