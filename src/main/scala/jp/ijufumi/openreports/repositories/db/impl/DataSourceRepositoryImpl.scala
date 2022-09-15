package jp.ijufumi.openreports.repositories.db.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.DataSource
import jp.ijufumi.openreports.entities.queries.{dataSourceQuery => query}
import jp.ijufumi.openreports.repositories.db.DataSourceRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DataSourceRepositoryImpl @Inject() (db: Database) extends DataSourceRepository {
  override def getById(id: String): Option[DataSource] = {
    val getDataSources = query
      .filter(_.id === id)
    val dataSources = Await.result(db.run(getDataSources.result), Duration("10s"))
    if (dataSources.isEmpty) {
      return None
    }
    Some(dataSources.head)
  }

  override def getAll(): Seq[DataSource] = Await.result(db.run(query.result), Duration("10s"))

  override def register(dataSource: DataSource): Option[DataSource] = {
    Await.result(db.run(query += dataSource), Duration("1m"))
    getById(dataSource.id)
  }

  override def update(dataSource: DataSource): Unit = {
    query.insertOrUpdate(dataSource)
  }
}
