package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.ReportGroupReport
import slick.jdbc.JdbcBackend.Database

trait ReportGroupReportRepository {

  def gets(
      db: Database,
      offset: Int,
      limit: Int,
  ): (Seq[ReportGroupReport], Int)

  def getById(db: Database, id: String): Option[ReportGroupReport]

  def getByIds(db: Database, ids: Seq[String]): Seq[ReportGroupReport]

  def register(db: Database, model: ReportGroupReport): Option[ReportGroupReport]

  def registerInBatch(db: Database, model: Seq[ReportGroupReport]): Seq[ReportGroupReport]

  def update(db: Database, model: ReportGroupReport): Unit

  def delete(db: Database, id: String): Unit

  def deleteByReportId(db: Database, id: String): Unit

  def deleteByReportGroupId(db: Database, id: String): Unit
}
