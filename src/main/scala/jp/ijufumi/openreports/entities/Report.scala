package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.entities.queries.reportTemplateQuery
import jp.ijufumi.openreports.utils.Dates

case class Report(
    id: String,
    name: String,
    reportTemplateId: String,
    dataSourceId: String,
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Reports(tag: Tag)
    extends Table[Report](
      tag,
      "reports",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def reportTemplateId = column[String]("report_template_id")
  def dataSourceId = column[String]("data_source_id")
  def workspaceId = column[String]("workspace_id")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  def template = foreignKey("report_templates", reportTemplateId, reportTemplateQuery)(_.id)

  override def * =
    (
      id,
      name,
      reportTemplateId,
      dataSourceId,
      workspaceId,
      createdAt,
      updatedAt,
      versions,
    ) <> (Report.tupled, Report.unapply)
}
