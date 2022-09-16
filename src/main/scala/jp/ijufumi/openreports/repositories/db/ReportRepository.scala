package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.{Report, ReportTemplate}

trait ReportRepository {

  def getById(id: String): Option[Report]

  def getWithTemplateById(id: String): Option[(Report, ReportTemplate)]

  def register(model: Report): Option[Report]

  def update(model: Report): Unit
}
