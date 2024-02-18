package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportGroupReportRepository
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl.queries.{
  reportGroupReportQuery => query,
}
import jp.ijufumi.openreports.presentation.models.responses.ReportGroupReport
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class ReportGroupReportRepositoryImpl @Inject() (db: Database) extends ReportGroupReportRepository {
  override def gets(
      offset: Int = 0,
      limit: Int = -1,
  ): (Seq[ReportGroupReport], Int) = {
    var filtered = query.drop(offset)
    val count = Await.result(db.run(query.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    (Await.result(db.run(filtered.result), queryTimeout).map(r => ReportGroupReport(r)), count)
  }

  override def getById(id: String): Option[ReportGroupReport] = {
    val getById = query
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head).map(r => ReportGroupReport(r))
  }

  override def getByIds(ids: Seq[String]): Seq[ReportGroupReport] = {
    val getById = query
      .filter(_.id.inSet(ids))
    Await.result(db.run(getById.result), queryTimeout).map(r => ReportGroupReport(r))
  }

  override def register(model: ReportGroupReport): Option[ReportGroupReport] = {
    val register = (query += model.toEntity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(model.id)
  }

  override def registerInBatch(model: Seq[ReportGroupReport]): Seq[ReportGroupReport] = {
    val register = (query ++= model.map(r => r.toEntity)).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    val ids = model.map(m => m.id)
    getByIds(ids)
  }

  override def update(model: ReportGroupReport): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = query.insertOrUpdate(newModel.toEntity).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(id: String): Unit = {
    val getById = query
      .filter(_.id === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }

  override def deleteByReportId(id: String): Unit = {
    val getById = query
      .filter(_.reportId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }

  override def deleteByReportGroupId(id: String): Unit = {
    val getById = query
      .filter(_.reportGroupId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
