package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.ReportReportParameterRepository
import jp.ijufumi.openreports.domain.models.entity.ReportReportParameter
import jp.ijufumi.openreports.exceptions.OptimisticLockException
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportReportParameterConverter
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportReportParameterConverter.conversions._
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  ReportReportParameter => ReportReportParameterEntity,
}
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
    val count = Await.result(db.run(reportReportParameterQuery.length.result), queryTimeout)
    val withOffset =
      if (offset > 0) reportReportParameterQuery.drop(offset) else reportReportParameterQuery
    val paged = if (limit > 0) withOffset.take(limit) else withOffset
    val rows = Await
      .result(db.run(paged.result), queryTimeout)
      .map(ReportReportParameterConverter.toDomain)
    (rows, count)
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
    Await
      .result(db.run(getById.result), queryTimeout)
      .map(r => ReportReportParameterConverter.toDomain(r))
  }

  override def register(
      db: Database,
      model: ReportReportParameter,
  ): Option[ReportReportParameter] = {
    val entity: jp.ijufumi.openreports.infrastructure.persistence.entity.ReportReportParameter =
      model
    val register = (reportReportParameterQuery += entity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.id)
  }

  override def registerInBatch(
      db: Database,
      model: Seq[ReportReportParameter],
  ): Seq[ReportReportParameter] = {
    val entities
        : Seq[jp.ijufumi.openreports.infrastructure.persistence.entity.ReportReportParameter] =
      model
    val register = (reportReportParameterQuery ++= entities).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    val ids = model.map(m => m.id)
    getByIds(db, ids)
  }

  override def update(db: Database, model: ReportReportParameter): Unit = {
    val newEntity: ReportReportParameterEntity =
      model.copy(updatedAt = Dates.currentTimestamp(), versions = model.versions + 1)
    val q = reportReportParameterQuery
      .filter(_.id === model.id)
      .filter(_.versions === model.versions)
    val affected = Await.result(db.run(q.update(newEntity).withPinnedSession), queryTimeout)
    if (affected == 0) {
      throw new OptimisticLockException(
        s"ReportReportParameter id=${model.id} was modified concurrently or does not exist",
      )
    }
  }

  override def delete(db: Database, id: String): Unit = {
    val getById = reportReportParameterQuery
      .filter(_.id === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }

  override def deleteByReportId(db: Database, id: String): Unit = {
    val getById = reportReportParameterQuery
      .filter(_.reportId === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }

  override def deleteByReportParameterId(db: Database, id: String): Unit = {
    val getById = reportReportParameterQuery
      .filter(_.reportParameterId === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
