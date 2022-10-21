package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.ReportTemplate

trait ReportTemplateRepository {

  def gets(workspaceId: String, offset: Int, limit: Int): (Seq[ReportTemplate], Int)

  def getById(workspaceId: String, id: String): Option[ReportTemplate]

  def register(model: ReportTemplate): Option[ReportTemplate]

  def update(model: ReportTemplate): Unit
}
