package jp.ijufumi.openreports.gateways.datastores.database.repositories

import jp.ijufumi.openreports.gateways.datastores.database.entities.ReportReportParameter

trait ReportReportParameterRepository {

  def gets(
      offset: Int,
      limit: Int,
  ): (Seq[ReportReportParameter], Int)

  def getById(id: String): Option[ReportReportParameter]

  def getByIds(ids: Seq[String]): Seq[ReportReportParameter]

  def register(model: ReportReportParameter): Option[ReportReportParameter]

  def registerInBatch(model: Seq[ReportReportParameter]): Seq[ReportReportParameter]

  def update(model: ReportReportParameter): Unit

  def delete(id: String): Unit

  def deleteByReportId(id: String): Unit

  def deleteByReportParameterId(id: String): Unit
}
