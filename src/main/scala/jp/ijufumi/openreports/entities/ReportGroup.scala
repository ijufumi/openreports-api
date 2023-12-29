package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates

case class ReportGroup(
    id: String,
    name: String,
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class ReportGroups(tag: Tag)
    extends Table[ReportGroup](
      tag,
      "report_groups",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def workspaceId = column[String]("workspace_id")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      workspaceId,
      createdAt,
      updatedAt,
      versions,
    ) <> (ReportGroup.tupled, ReportGroup.unapply)
}
