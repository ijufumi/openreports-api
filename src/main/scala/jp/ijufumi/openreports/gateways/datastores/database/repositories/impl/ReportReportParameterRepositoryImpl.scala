package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.gateways.datastores.database.repositories.ReportReportParameterRepository
import jp.ijufumi.openreports.gateways.datastores.database.repositories.impl.queries.{
  reportReportParameterQuery => query,
}
import jp.ijufumi.openreports.interfaces.models.outputs.ReportReportParameter
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class ReportReportParameterRepositoryImpl @Inject() (db: Database)
    extends ReportReportParameterRepository {
  override def gets(
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

  override def getById(id: String): Option[ReportReportParameter] = {
    val getById = query
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head).map(r => ReportReportParameter(r))
  }

  override def getByIds(ids: Seq[String]): Seq[ReportReportParameter] = {
    val getById = query
      .filter(_.id.inSet(ids))
    Await.result(db.run(getById.result), queryTimeout).map(r => ReportReportParameter(r))
  }

  override def register(model: ReportReportParameter): Option[ReportReportParameter] = {
    val register = (query += model.toEntity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(model.id)
  }

  override def registerInBatch(model: Seq[ReportReportParameter]): Seq[ReportReportParameter] = {
    val register = (query ++= model.map(r => r.toEntity)).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    val ids = model.map(m => m.id)
    getByIds(ids)
  }

  override def update(model: ReportReportParameter): Unit = {
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

  override def deleteByReportParameterId(id: String): Unit = {
    val getById = query
      .filter(_.reportParameterId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
