package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Function
import slick.jdbc.JdbcBackend.Database

trait FunctionRepository {
  def getAll(db: Database): Seq[Function]

  def getsByIds(db: Database, ids: Seq[String]): Seq[Function]
}
