package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportGroupReportRepository
import jp.ijufumi.openreports.domain.models.entity.ReportGroupReport
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.domain.models.entity.ReportGroupReport.conversions._

import scala.concurrent.Await

class ReportGroupReportRepositoryImpl extends ReportGroupReportRepository {
  override def gets(
      db: Database,
      offset: Int = 0,
      limit: Int = -1,
  ): (Seq[ReportGroupReport], Int) = {
    var filtered = reportGroupReportQuery.drop(offset)
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    val result = Await.result(db.run(filtered.result), queryTimeout)
    (result, count)
  }

  override def getById(db: Database, id: String): Option[ReportGroupReport] = {
    val getById = reportGroupReportQuery
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def getByIds(db: Database, ids: Seq[String]): Seq[ReportGroupReport] = {
    val getById = reportGroupReportQuery
      .filter(_.id.inSet(ids))
    Await.result(db.run(getById.result), queryTimeout).map(r => ReportGroupReport(r))
  }

  override def register(db: Database, model: ReportGroupReport): Option[ReportGroupReport] = {
    val register = (reportGroupReportQuery += model.toEntity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.id)
  }

  override def registerInBatch(
      db: Database,
      model: Seq[ReportGroupReport],
  ): Seq[ReportGroupReport] = {
    val register = (reportGroupReportQuery ++= model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    val ids = model.map(m => m.id)
    getByIds(db, ids)
  }

  override def update(db: Database, model: ReportGroupReport): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = reportGroupReportQuery.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(db: Database, id: String): Unit = {
    val getById = reportGroupReportQuery
      .filter(_.id === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }

  override def deleteByReportId(db: Database, id: String): Unit = {
    val getById = reportGroupReportQuery
      .filter(_.reportId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }

  override def deleteByReportGroupId(db: Database, id: String): Unit = {
    val getById = reportGroupReportQuery
      .filter(_.reportGroupId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
