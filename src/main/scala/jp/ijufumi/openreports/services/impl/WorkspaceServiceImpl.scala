package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.{Workspace, WorkspaceMember}
import jp.ijufumi.openreports.repositories.db.{WorkspaceMemberRepository, WorkspaceRepository}
import jp.ijufumi.openreports.services.WorkspaceService
import jp.ijufumi.openreports.utils.{IDs, Strings}
import slick.jdbc.PostgresProfile.api._

class WorkspaceServiceImpl @Inject() (
    workspaceRepository: WorkspaceRepository,
    workspaceMemberRepository: WorkspaceMemberRepository,
) extends WorkspaceService {
  override def createAndRelevant(memberId: String, email: String): Option[Workspace] = {
    try {
      val workspaceName = Strings.nameFromEmail(email) + "'s workspace"
      val workspace = Workspace(IDs.ulid(), workspaceName, Strings.generateSlug())
      workspaceRepository.register(workspace)
      val workspaceMember = WorkspaceMember(workspace.id, memberId)
      workspaceMemberRepository.register(workspaceMember)
    } catch {
      case e: Throwable =>
        SimpleDBIO(_.connection.rollback()).withPinnedSession
        throw e
    }

    None
  }
}
