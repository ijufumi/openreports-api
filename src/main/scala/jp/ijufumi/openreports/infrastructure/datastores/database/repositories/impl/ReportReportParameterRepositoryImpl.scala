package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.ReportReportParameterRepository
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl.queries.{
  reportReportParameterQuery => query,
}
import jp.ijufumi.openreports.domain.models.entity.ReportReportParameter
import jp.ijufumi.openreports.domain.models.entity.ReportReportParameter.conversions._
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class ReportReportParameterRepositoryImpl extends ReportReportParameterRepository {
  override def gets(
      db: Database,
      offset: Int = 0,
      limit: Int = -1,
  ): (Seq[ReportReportParameter], Int) = {
    var filtered = query.drop(offset)
    val count = Await.result(db.run(query.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.take(limit)
    }
    (Await.result(db.run(filtered.result), queryTimeout).map(r => ReportReportParameter(r)), count)
  }

  override def getById(db: Database, id: String): Option[ReportReportParameter] = {
    val getById = query
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head).map(r => ReportReportParameter(r))
  }

  override def getByIds(db: Database, ids: Seq[String]): Seq[ReportReportParameter] = {
    val getById = query
      .filter(_.id.inSet(ids))
    Await.result(db.run(getById.result), queryTimeout).map(r => ReportReportParameter(r))
  }

  override def register(
      db: Database,
      model: ReportReportParameter,
  ): Option[ReportReportParameter] = {
    val register = (query += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.id)
  }

  override def registerInBatch(
      db: Database,
      model: Seq[ReportReportParameter],
  ): Seq[ReportReportParameter] = {
    val register = (query ++= model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    val ids = model.map(m => m.id)
    getByIds(db, ids)
  }

  override def update(db: Database, model: ReportReportParameter): Unit = {
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

  override def deleteByReportParameterId(db: Database, id: String): Unit = {
    val getById = query
      .filter(_.reportParameterId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
