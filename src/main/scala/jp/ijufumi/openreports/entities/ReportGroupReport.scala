package jp.ijufumi.openreports.entities

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
    extends Table[ReportGroupReport](
      tag,
      "report_group_reports",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def reportId = column[String]("report_id")
  def reportGroupId = column[String]("report_group_id")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

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
