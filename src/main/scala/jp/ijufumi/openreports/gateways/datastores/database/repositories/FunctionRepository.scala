package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.gateways.datastores.database.entities.Function

trait FunctionRepository {
  def getAll: Seq[Function]

  def getsByIds(ids: Seq[String]): Seq[Function]
}
