package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.entities.ReportGroupReport

trait ReportGroupReportRepository {

  def gets(
      offset: Int,
      limit: Int,
  ): (Seq[ReportGroupReport], Int)

  def getById(id: String): Option[ReportGroupReport]

  def register(model: ReportGroupReport): Option[ReportGroupReport]

  def update(model: ReportGroupReport): Unit

  def delete(id: String): Unit
}
