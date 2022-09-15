package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.ReportTemplate

trait ReportTemplateRepository {

  def getById(id: String): Option[ReportTemplate]

  def register(model: ReportTemplate): Option[ReportTemplate]

  def update(model: ReportTemplate): Unit
}
