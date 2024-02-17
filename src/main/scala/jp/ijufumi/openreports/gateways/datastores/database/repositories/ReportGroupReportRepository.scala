package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.interfaces.models.outputs.ReportGroupReport

trait ReportGroupReportRepository {

  def gets(
      offset: Int,
      limit: Int,
  ): (Seq[ReportGroupReport], Int)

  def getById(id: String): Option[ReportGroupReport]

  def getByIds(ids: Seq[String]): Seq[ReportGroupReport]

  def register(model: ReportGroupReport): Option[ReportGroupReport]

  def registerInBatch(model: Seq[ReportGroupReport]): Seq[ReportGroupReport]

  def update(model: ReportGroupReport): Unit

  def delete(id: String): Unit

  def deleteByReportId(id: String): Unit

  def deleteByReportGroupId(id: String): Unit
}
