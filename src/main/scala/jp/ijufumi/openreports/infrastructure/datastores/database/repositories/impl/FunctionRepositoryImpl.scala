package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.FunctionRepository
import jp.ijufumi.openreports.domain.models.entity.Function
import jp.ijufumi.openreports.domain.models.entity.Function.conversions._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class FunctionRepositoryImpl extends FunctionRepository {
  override def getAll(db: Database): Seq[Function] = {
    val result = Await.result(db.run(functionQuery.result), queryTimeout)
    result
  }

  override def getsByIds(db: Database, ids: Seq[String]): Seq[Function] = {
    val result = Await.result(db.run(functionQuery.filter(_.id.inSet(ids)).result), queryTimeout)
    result
  }
}
