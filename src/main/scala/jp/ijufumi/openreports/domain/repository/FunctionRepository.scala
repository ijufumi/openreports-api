package jp.ijufumi.openreports.domain.repository

import jp.ijufumi.openreports.domain.models.entity.Function
import slick.jdbc.JdbcBackend.Database

trait FunctionRepository {
  def getAll(db: Database): Seq[Function]

  def getsByIds(db: Database, ids: Seq[String]): Seq[Function]
}
