package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.gateways.datastores.database.entities.{DataSource, DriverType}
import queries.{dataSourceQuery => query, driverTypeQuery}
import jp.ijufumi.openreports.gateways.datastores.database.repositories.DataSourceRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class DataSourceRepositoryImpl @Inject() (db: Database) extends DataSourceRepository {
  override def getById(workspaceId: String, id: String): Option[DataSource] = {
    val getDataSources = query
      .filter(_.id === id)
      .filter(_.workspaceId === workspaceId)
    val dataSources = Await.result(db.run(getDataSources.result), queryTimeout)
    Some(dataSources.head)
  }

  override def getAll(workspaceId: String): Seq[DataSource] = {
    val getDataSources = query
      .filter(_.workspaceId === workspaceId)
    Await.result(db.run(getDataSources.result), queryTimeout)
  }

  override def getByIdWithDriverType(
      workspaceId: String,
      id: String,
  ): Option[(DataSource, DriverType)] = {
    val getDataSources = query
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

  override def getAllWithDriverType(workspaceId: String): Seq[(DataSource, DriverType)] = {
    val getDataSources = query
      .join(driverTypeQuery)
      .on(_.driverTypeId === _.id)
      .filter(_._1.workspaceId === workspaceId)
    Await.result(db.run(getDataSources.result), queryTimeout)
  }

  override def register(dataSource: DataSource): Option[DataSource] = {
    Await.result(db.run((query += dataSource).withPinnedSession), queryTimeout)
    getById(dataSource.workspaceId, dataSource.id)
  }

  override def update(dataSource: DataSource): Unit = {
    query.insertOrUpdate(dataSource).withPinnedSession
  }

  override def delete(workspaceId: String, id: String): Unit = {
    val getDataSources = query
      .filter(_.id === id)
      .filter(_.workspaceId === workspaceId)

    Await.result(db.run(getDataSources.delete.withPinnedSession), queryTimeout)
  }
}
