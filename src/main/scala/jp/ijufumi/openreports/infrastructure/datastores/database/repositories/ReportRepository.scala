package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Report
import slick.jdbc.JdbcBackend.Database

trait ReportRepository {

  def gets(
            db: Database,
      workspaceId: String,
      offset: Int,
      limit: Int,
      templateId: String = "",
  ): (Seq[Report], Int)

  def getsWithTemplate(
                        db: Database,
      workspaceId: String,
      offset: Int,
      limit: Int,
      templateId: String = "",
  ): (Seq[Report], Int)

  def getById(db: Database,workspaceId: String, id: String): Option[Report]

  def getByIdWithTemplate(db: Database,workspaceId: String, id: String): Option[Report]

  def register(db: Database,model: Report): Option[Report]

  def update(db: Database,model: Report): Unit

  def delete(db: Database,workspaceId: String, id: String): Unit
}
