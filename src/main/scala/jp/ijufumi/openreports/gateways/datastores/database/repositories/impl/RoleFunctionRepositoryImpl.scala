package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.gateways.datastores.database.repositories.RoleFunctionRepository
import jp.ijufumi.openreports.gateways.datastores.database.repositories.impl.queries.{
  roleFunctionQuery => query,
}
import jp.ijufumi.openreports.interfaces.models.outputs.RoleFunction
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class RoleFunctionRepositoryImpl @Inject() (db: Database) extends RoleFunctionRepository {
  override def getAll: Seq[RoleFunction] = {
    Await.result(db.run(query.result), queryTimeout).map(r => RoleFunction(r))
  }

  override def getByRoleId(roleId: String): Seq[RoleFunction] = {
    Await
      .result(db.run(query.filter(_.roleId === roleId).result), queryTimeout)
      .map(r => RoleFunction(r))
  }

  override def getByFunctionId(functionId: String): Seq[RoleFunction] = {
    Await
      .result(db.run(query.filter(_.functionId === functionId).result), queryTimeout)
      .map(r => RoleFunction(r))
  }
}
