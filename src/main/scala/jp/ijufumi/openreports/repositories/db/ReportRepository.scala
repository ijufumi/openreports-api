package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.{Report, ReportTemplate}

trait ReportRepository {

  def gets(offset: Int, limit: Int): (Seq[Report], Int)

  def getsWithTemplate(offset: Int, limit: Int): (Seq[(Report, ReportTemplate)], Int)

  def getById(id: String): Option[Report]

  def getByIdWithTemplate(id: String): Option[(Report, ReportTemplate)]

  def register(model: Report): Option[Report]

  def update(model: Report): Unit
}
