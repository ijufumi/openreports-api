package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.ReportReportParameter
import slick.jdbc.JdbcBackend.Database

trait ReportReportParameterRepository {

  def gets(
      db: Database,
      offset: Int,
      limit: Int,
  ): (Seq[ReportReportParameter], Int)

  def getById(db: Database, id: String): Option[ReportReportParameter]

  def getByIds(db: Database, ids: Seq[String]): Seq[ReportReportParameter]

  def register(db: Database, model: ReportReportParameter): Option[ReportReportParameter]

  def registerInBatch(db: Database, model: Seq[ReportReportParameter]): Seq[ReportReportParameter]

  def update(db: Database, model: ReportReportParameter): Unit

  def delete(db: Database, id: String): Unit

  def deleteByReportId(db: Database, id: String): Unit

  def deleteByReportParameterId(db: Database, id: String): Unit
}
