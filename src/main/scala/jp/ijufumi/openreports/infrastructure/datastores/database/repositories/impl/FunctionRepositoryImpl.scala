package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.FunctionRepository
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl.queries.{
  functionQuery => query,
}
import jp.ijufumi.openreports.domain.models.entity.Function
import jp.ijufumi.openreports.domain.models.entity.Function.conversions._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class FunctionRepositoryImpl @Inject() (db: Database) extends FunctionRepository {
  override def getAll: Seq[Function] = {
    val result = Await.result(db.run(query.result), queryTimeout)
    result
  }

  override def getsByIds(ids: Seq[String]): Seq[Function] = {
    val result = Await.result(db.run(query.filter(_.id.inSet(ids)).result), queryTimeout)
    result
  }
}
