package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.ReportReportParameterRepository
import jp.ijufumi.openreports.domain.models.entity.ReportReportParameter
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportReportParameterConverter
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportReportParameterConverter.conversions._
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
    var filtered = reportReportParameterQuery.drop(0)
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    if (limit > 0) {
      filtered = filtered.drop(offset).take(limit)
    }
    (Await.result(db.run(filtered.result), queryTimeout).map(r => ReportReportParameterConverter.toDomain(r)), count)
  }

  override def getById(db: Database, id: String): Option[ReportReportParameter] = {
    val getById = reportReportParameterQuery
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(ReportReportParameterConverter.toDomain(models.head))
  }

  override def getByIds(db: Database, ids: Seq[String]): Seq[ReportReportParameter] = {
    val getById = reportReportParameterQuery
      .filter(_.id.inSet(ids))
    Await.result(db.run(getById.result), queryTimeout).map(r => ReportReportParameterConverter.toDomain(r))
  }

  override def register(
      db: Database,
      model: ReportReportParameter,
  ): Option[ReportReportParameter] = {
    val entity: jp.ijufumi.openreports.infrastructure.persistence.entity.ReportReportParameter = model
    val register = (reportReportParameterQuery += entity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.id)
  }

  override def registerInBatch(
      db: Database,
      model: Seq[ReportReportParameter],
  ): Seq[ReportReportParameter] = {
    val entities: Seq[jp.ijufumi.openreports.infrastructure.persistence.entity.ReportReportParameter] = model
    val register = (reportReportParameterQuery ++= entities).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    val ids = model.map(m => m.id)
    getByIds(db, ids)
  }

  override def update(db: Database, model: ReportReportParameter): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val entity: jp.ijufumi.openreports.infrastructure.persistence.entity.ReportReportParameter = newModel
    val updateQuery = reportReportParameterQuery.insertOrUpdate(entity).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(db: Database, id: String): Unit = {
    val getById = reportReportParameterQuery
      .filter(_.id === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }

  override def deleteByReportId(db: Database, id: String): Unit = {
    val getById = reportReportParameterQuery
      .filter(_.reportId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }

  override def deleteByReportParameterId(db: Database, id: String): Unit = {
    val getById = reportReportParameterQuery
      .filter(_.reportParameterId === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
