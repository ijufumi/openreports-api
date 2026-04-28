package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.ReportGroupReportRepository
import jp.ijufumi.openreports.domain.models.entity.ReportGroupReport
import jp.ijufumi.openreports.exceptions.OptimisticLockException
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportGroupReportConverter.conversions._
import jp.ijufumi.openreports.infrastructure.persistence.entity.{
  ReportGroupReport => ReportGroupReportEntity,
}

import scala.concurrent.Await

class ReportGroupReportRepositoryImpl extends ReportGroupReportRepository {
  override def gets(
      db: Database,
      offset: Int = 0,
      limit: Int = -1,
  ): (Seq[ReportGroupReport], Int) = {
    val count = Await.result(db.run(reportGroupReportQuery.length.result), queryTimeout)
    val withOffset =
      if (offset > 0) reportGroupReportQuery.drop(offset) else reportGroupReportQuery
    val paged = if (limit > 0) withOffset.take(limit) else withOffset
    val result = Await.result(db.run(paged.result), queryTimeout)
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
    Await
      .result(db.run(getById.result), queryTimeout)
      .map(r =>
        jp.ijufumi.openreports.infrastructure.persistence.converter.ReportGroupReportConverter
          .toDomain(r),
      )
  }

  override def register(db: Database, model: ReportGroupReport): Option[ReportGroupReport] = {
    val entity: jp.ijufumi.openreports.infrastructure.persistence.entity.ReportGroupReport = model
    val register = (reportGroupReportQuery += entity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.id)
  }

  override def registerInBatch(
      db: Database,
      model: Seq[ReportGroupReport],
  ): Seq[ReportGroupReport] = {
    val entities: Seq[jp.ijufumi.openreports.infrastructure.persistence.entity.ReportGroupReport] =
      model
    val register = (reportGroupReportQuery ++= entities).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    val ids = model.map(m => m.id)
    getByIds(db, ids)
  }

  override def update(db: Database, model: ReportGroupReport): Unit = {
    val newEntity: ReportGroupReportEntity =
      model.copy(updatedAt = Dates.currentTimestamp(), versions = model.versions + 1)
    val q = reportGroupReportQuery
      .filter(_.id === model.id)
      .filter(_.versions === model.versions)
    val affected = Await.result(db.run(q.update(newEntity).withPinnedSession), queryTimeout)
    if (affected == 0) {
      throw new OptimisticLockException(
        s"ReportGroupReport id=${model.id} was modified concurrently or does not exist",
      )
    }
  }

  override def delete(db: Database, id: String): Unit = {
    val getById = reportGroupReportQuery
      .filter(_.id === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }

  override def deleteByReportId(db: Database, id: String): Unit = {
    val getById = reportGroupReportQuery
      .filter(_.reportId === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }

  override def deleteByReportGroupId(db: Database, id: String): Unit = {
    val getById = reportGroupReportQuery
      .filter(_.reportGroupId === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
