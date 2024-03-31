package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportGroupReportRepository
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl.queries.{
  reportGroupReportQuery => query,
}
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
    var filtered = query.drop(offset)
    val count = Await.result(db.run(query.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    val result = Await.result(db.run(filtered.result), queryTimeout)
    (result, count)
  }

  override def getById(db: Database, id: String): Option[ReportGroupReport] = {
    val getById = query
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def getByIds(db: Database, ids: Seq[String]): Seq[ReportGroupReport] = {
    val getById = query
      .filter(_.id.inSet(ids))
    Await.result(db.run(getById.result), queryTimeout).map(r => ReportGroupReport(r))
  }

  override def register(db: Database, model: ReportGroupReport): Option[ReportGroupReport] = {
    val register = (query += model.toEntity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.id)
  }

  override def registerInBatch(
      db: Database,
      model: Seq[ReportGroupReport],
  ): Seq[ReportGroupReport] = {
    val register = (query ++= model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    val ids = model.map(m => m.id)
    getByIds(db, ids)
  }

  override def update(db: Database, model: ReportGroupReport): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = query.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(db: Database, id: String): Unit = {
    val getById = query
      .filter(_.id === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }

  override def deleteByReportId(db: Database, id: String): Unit = {
    val getById = query
      .filter(_.reportId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }

  override def deleteByReportGroupId(db: Database, id: String): Unit = {
    val getById = query
      .filter(_.reportGroupId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
