package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.{Report, ReportTemplate}

trait ReportRepository {

  def gets(offset: Int, limit: Int): Seq[Report]

  def getsWithTemplate(offset: Int, limit: Int): Seq[(Report, ReportTemplate)]

  def getById(id: String): Option[Report]

  def getByIdWithTemplate(id: String): Option[(Report, ReportTemplate)]

  def register(model: Report): Option[Report]

  def update(model: Report): Unit
}
