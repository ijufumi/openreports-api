package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.ReportTemplate

trait ReportTemplateRepository {

  def gets(offset: Int, limit: Int): (Seq[ReportTemplate], Int)

  def getById(id: String): Option[ReportTemplate]

  def register(model: ReportTemplate): Option[ReportTemplate]

  def update(model: ReportTemplate): Unit
}
