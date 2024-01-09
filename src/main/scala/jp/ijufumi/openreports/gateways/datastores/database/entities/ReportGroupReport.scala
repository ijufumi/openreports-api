package jp.ijufumi.openreports.gateways.datastores.database.entities

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates

case class ReportGroupReport(
    id: String,
    reportId: String,
    reportGroupId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class ReportGroupReports(tag: Tag)
    extends EntityBase[ReportGroupReport](
      tag,
      "report_group_reports",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def reportId = column[String]("report_id")
  def reportGroupId = column[String]("report_group_id")

  override def * =
    (
      id,
      reportId,
      reportGroupId,
      createdAt,
      updatedAt,
      versions,
    ) <> (ReportGroupReport.tupled, ReportGroupReport.unapply)
}
