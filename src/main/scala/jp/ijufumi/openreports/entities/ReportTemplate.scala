package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime

case class ReportTemplate(
    id: String,
    name: String,
    filePath: String,
    workspaceId: String,
    createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
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
  def workspaceId = column[String]("workspaceId")
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")
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
