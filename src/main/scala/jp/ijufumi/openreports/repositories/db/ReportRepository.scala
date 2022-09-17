package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.{DataSource, Report, ReportTemplate}

trait ReportRepository {

  def gets(): Seq[Report]

  def getsWithTemplate: Seq[(Report, ReportTemplate, DataSource)]

  def getById(id: String): Option[Report]

  def getWithTemplateById(id: String): Option[(Report, ReportTemplate, DataSource)]

  def register(model: Report): Option[Report]

  def update(model: Report): Unit
}
