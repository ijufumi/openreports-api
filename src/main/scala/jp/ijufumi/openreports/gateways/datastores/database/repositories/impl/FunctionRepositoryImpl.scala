package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.gateways.datastores.database.entities.Function
import jp.ijufumi.openreports.gateways.datastores.database.repositories.FunctionRepository
import jp.ijufumi.openreports.gateways.datastores.database.repositories.impl.queries.{
  functionQuery => query,
}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class FunctionRepositoryImpl @Inject() (db: Database) extends FunctionRepository {
  override def getAll: Seq[Function] = {
    Await.result(db.run(query.result), queryTimeout)
  }
}
