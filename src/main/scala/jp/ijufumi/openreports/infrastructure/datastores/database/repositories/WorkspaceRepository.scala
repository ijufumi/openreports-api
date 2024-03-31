package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Workspace
import slick.jdbc.JdbcBackend.Database

trait WorkspaceRepository {

  def getById(db: Database, id: String): Option[Workspace]

  def getsByMemberId(db: Database, memberId: String): Seq[Workspace]

  def register(db: Database, workspace: Workspace): Option[Workspace]

  def update(db: Database, workspace: Workspace): Option[Workspace]
}
