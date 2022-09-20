package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.{Report, ReportTemplate}

trait ReportRepository {

  def gets(): Seq[Report]

  def getsWithTemplate: Seq[(Report, ReportTemplate)]

  def getById(id: String): Option[Report]

  def getByIdWithTemplate(id: String): Option[(Report, ReportTemplate)]

  def register(model: Report): Option[Report]

  def update(model: Report): Unit
}
