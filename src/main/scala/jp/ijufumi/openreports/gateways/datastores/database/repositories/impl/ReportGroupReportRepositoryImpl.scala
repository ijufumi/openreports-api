package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.ReportGroupReport
import jp.ijufumi.openreports.gateways.datastores.database.repositories.ReportGroupReportRepository
import jp.ijufumi.openreports.gateways.datastores.database.repositories.impl.queries.{
  reportGroupReportQuery => query,
}
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
    (Await.result(db.run(filtered.result), queryTimeout), count)
  }

  override def getById(id: String): Option[ReportGroupReport] = {
    val getById = query
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head)
  }

  override def register(model: ReportGroupReport): Option[ReportGroupReport] = {
    val register = (query += model).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(model.id)
  }

  override def update(model: ReportGroupReport): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = query.insertOrUpdate(newModel).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }

  override def delete(id: String): Unit = {
    val getById = query
      .filter(_.id === id)
    Await.result(db.run(getById.delete), queryTimeout)
  }
}
