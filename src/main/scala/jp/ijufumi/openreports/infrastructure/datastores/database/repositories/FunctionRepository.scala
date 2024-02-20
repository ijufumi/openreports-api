package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.domain.models.entity.Function

trait FunctionRepository {
  def getAll: Seq[Function]

  def getsByIds(ids: Seq[String]): Seq[Function]
}
