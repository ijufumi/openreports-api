package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.WorkspaceMember
import slick.dbio.{DBIOAction, Effect, NoStream}

trait WorkspaceMemberRepository {
  def getById(workspaceId: String, memberId: String): Option[WorkspaceMember]

  def register(workspaceMember: WorkspaceMember): Option[WorkspaceMember]

  def registerTransactional(workspaceMember: WorkspaceMember): DBIOAction[Int, NoStream, Effect.Write]
}
