package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class ReportTemplate(
    id: String,
    name: String,
    filePath: String,
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class ReportTemplates(tag: Tag)
    extends Table[ReportTemplate](
      tag,
      "report_templates",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def filePath = column[String]("file_path")
  def workspaceId = column[String]("workspace_id")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      filePath,
      workspaceId,
      createdAt,
      updatedAt,
      versions,
    ) <> (ReportTemplate.tupled, ReportTemplate.unapply)
}
