package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.RoleFunctionRepository
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl.queries.{
  roleFunctionQuery => query,
}
import jp.ijufumi.openreports.domain.models.entity.RoleFunction
import jp.ijufumi.openreports.domain.models.entity.RoleFunction.conversions._
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class RoleFunctionRepositoryImpl extends RoleFunctionRepository {
  override def getAll(db: Database): Seq[RoleFunction] = {
    val result = Await.result(db.run(query.result), queryTimeout)
    result
  }

  override def getByRoleId(db: Database, roleId: String): Seq[RoleFunction] = {
    val result = Await
      .result(db.run(query.filter(_.roleId === roleId).result), queryTimeout)
    result
  }

  override def getByFunctionId(db: Database, functionId: String): Seq[RoleFunction] = {
    val result = Await
      .result(db.run(query.filter(_.functionId === functionId).result), queryTimeout)
    result
  }
}
