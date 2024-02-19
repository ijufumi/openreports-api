package jp.ijufumi.openreports.infrastructure.datastores.database.repositories

import jp.ijufumi.openreports.presentation.models.responses.ReportParameter

trait ReportParameterRepository {

  def gets(
      workspaceId: String,
      offset: Int,
      limit: Int,
  ): (Seq[ReportParameter], Int)

  def getById(workspaceId: String, id: String): Option[ReportParameter]

  def register(model: ReportParameter): Option[ReportParameter]

  def update(model: ReportParameter): Unit

  def delete(workspaceId: String, id: String): Unit
}
