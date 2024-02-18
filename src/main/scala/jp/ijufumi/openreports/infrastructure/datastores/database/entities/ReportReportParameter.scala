package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates

case class ReportReportParameter(
    id: String,
    reportId: String,
    reportParameterId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class ReportReportParameters(tag: Tag)
    extends EntityBase[ReportReportParameter](
      tag,
      "report_report_parameters",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def reportId = column[String]("report_id")
  def reportParameterId = column[String]("report_parameter_id")

  override def * =
    (
      id,
      reportId,
      reportParameterId,
      createdAt,
      updatedAt,
      versions,
    ) <> (ReportReportParameter.tupled, ReportReportParameter.unapply)
}
