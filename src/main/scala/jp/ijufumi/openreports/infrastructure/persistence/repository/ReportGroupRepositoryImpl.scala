package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.ReportGroupRepository
import jp.ijufumi.openreports.domain.models.entity.ReportGroup
import jp.ijufumi.openreports.exceptions.OptimisticLockException
import jp.ijufumi.openreports.infrastructure.persistence.converter.ReportGroupConverter.conversions._
import jp.ijufumi.openreports.infrastructure.persistence.entity.{ReportGroup => ReportGroupEntity}
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class ReportGroupRepositoryImpl extends ReportGroupRepository {
  override def gets(
      db: Database,
      workspaceId: String,
      offset: Int = 0,
      limit: Int = -1,
  ): (Seq[ReportGroup], Int) = {
    val filtered = reportGroupQuery.filter(_.workspaceId === workspaceId)
    val count = Await.result(db.run(filtered.length.result), queryTimeout)
    val withOffset = if (offset > 0) filtered.drop(offset) else filtered
    val paged = if (limit > 0) withOffset.take(limit) else withOffset
    val result = Await.result(db.run(paged.result), queryTimeout)
    (result, count)
  }

  override def getById(db: Database, workspaceId: String, id: String): Option[ReportGroup] = {
    val getById = reportGroupQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def register(db: Database, model: ReportGroup): Option[ReportGroup] = {
    val register = (reportGroupQuery += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(db, model.workspaceId, model.id)
  }

  override def update(db: Database, model: ReportGroup): Unit = {
    val newEntity: ReportGroupEntity =
      model.copy(updatedAt = Dates.currentTimestamp(), versions = model.versions + 1)
    val q = reportGroupQuery.filter(_.id === model.id).filter(_.versions === model.versions)
    val affected = Await.result(db.run(q.update(newEntity).withPinnedSession), queryTimeout)
    if (affected == 0) {
      throw new OptimisticLockException(
        s"ReportGroup id=${model.id} was modified concurrently or does not exist",
      )
    }
  }

  override def delete(db: Database, workspaceId: String, id: String): Unit = {
    val getById = reportGroupQuery
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    Await.result(db.run(getById.delete.withPinnedSession), queryTimeout)
  }
}
