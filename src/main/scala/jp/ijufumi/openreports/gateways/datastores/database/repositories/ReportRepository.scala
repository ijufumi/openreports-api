package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.interfaces.models.outputs.Report

trait ReportRepository {

  def gets(
      workspaceId: String,
      offset: Int,
      limit: Int,
      templateId: String = "",
  ): (Seq[Report], Int)

  def getsWithTemplate(
      workspaceId: String,
      offset: Int,
      limit: Int,
      templateId: String = "",
  ): (Seq[Report], Int)

  def getById(workspaceId: String, id: String): Option[Report]

  def getByIdWithTemplate(workspaceId: String, id: String): Option[Report]

  def register(model: Report): Option[Report]

  def update(model: Report): Unit

  def delete(workspaceId: String, id: String): Unit
}
