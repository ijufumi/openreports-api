package jp.ijufumi.openreports.infrastructure.persistence.repository

import jp.ijufumi.openreports.domain.repository.DataSourceRepository
import jp.ijufumi.openreports.domain.models.entity.DataSource
import jp.ijufumi.openreports.exceptions.OptimisticLockException
import jp.ijufumi.openreports.infrastructure.persistence.converter.DataSourceConverter.conversions._
import jp.ijufumi.openreports.infrastructure.persistence.entity.{DataSource => DataSourceEntity}
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class DataSourceRepositoryImpl extends DataSourceRepository {
  override def getById(db: Database, workspaceId: String, id: String): Option[DataSource] = {
    val getDataSources = dataSourceQuery
      .filter(_.id === id)
      .filter(_.workspaceId === workspaceId)
    val dataSources = Await.result(db.run(getDataSources.result), queryTimeout)
    dataSources.headOption
  }

  override def getAll(db: Database, workspaceId: String): Seq[DataSource] = {
    val getDataSources = dataSourceQuery
      .filter(_.workspaceId === workspaceId)
    // using variable is needed to use implicit converting
    val result = Await.result(db.run(getDataSources.result), queryTimeout)
    result
  }

  override def getByIdWithDriverType(
      db: Database,
      workspaceId: String,
      id: String,
  ): Option[DataSource] = {
    val getDataSources = dataSourceQuery
      .join(driverTypeQuery)
      .on(_.driverTypeId === _.id)
      .filter(_._1.id === id)
      .filter(_._1.workspaceId === workspaceId)
    val dataSources = Await.result(db.run(getDataSources.result), queryTimeout)
    if (dataSources.isEmpty) {
      return None
    }
    Some(dataSources.head)
  }

  override def getAllWithDriverType(db: Database, workspaceId: String): Seq[DataSource] = {
    val getDataSources = dataSourceQuery
      .join(driverTypeQuery)
      .on(_.driverTypeId === _.id)
      .filter(_._1.workspaceId === workspaceId)
    // using variable is needed to use implicit converting
    val result = Await.result(db.run(getDataSources.result), queryTimeout)
    result
  }

  override def register(db: Database, dataSource: DataSource): Option[DataSource] = {
    Await.result(db.run((dataSourceQuery += dataSource).withPinnedSession), queryTimeout)
    getById(db, dataSource.workspaceId, dataSource.id)
  }

  override def update(db: Database, dataSource: DataSource): Unit = {
    val newEntity: DataSourceEntity =
      dataSource.copy(updatedAt = Dates.currentTimestamp(), versions = dataSource.versions + 1)
    val q = dataSourceQuery
      .filter(_.id === dataSource.id)
      .filter(_.versions === dataSource.versions)
    val affected = Await.result(db.run(q.update(newEntity).withPinnedSession), queryTimeout)
    if (affected == 0) {
      throw new OptimisticLockException(
        s"DataSource id=${dataSource.id} was modified concurrently or does not exist",
      )
    }
  }

  override def delete(db: Database, workspaceId: String, id: String): Unit = {
    val getDataSources = dataSourceQuery
      .filter(_.id === id)
      .filter(_.workspaceId === workspaceId)

    Await.result(db.run(getDataSources.delete.withPinnedSession), queryTimeout)
  }
}
