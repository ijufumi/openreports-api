package jp.ijufumi.openreports.injector

import com.google.inject.AbstractModule
import jp.ijufumi.openreports.repositories._
import jp.ijufumi.openreports.repositories.db.{MemberRepository, MemberRepositoryImpl}
import jp.ijufumi.openreports.repositories.db.impl._

class RepositoryModule extends AbstractModule {
  override def configure(): Unit = {
    super.configure()
    bind(classOf[MemberRepository])
      .to(classOf[MemberRepositoryImpl])
  }
}
