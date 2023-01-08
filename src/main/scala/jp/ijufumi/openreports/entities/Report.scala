package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.models.inputs.UpdateReport
import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates

case class Report(
    id: String,
    name: String,
    templateId: String,
    dataSourceId: Option[String],
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def copyForUpdate(input: UpdateReport): Report = {
    this.copy(name = input.name, templateId = input.templateId)
  }
}

class Reports(tag: Tag)
    extends Table[Report](
      tag,
      "reports",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def templateId = column[String]("template_id")
  def dataSourceId = column[Option[String]]("data_source_id")
  def workspaceId = column[String]("workspace_id")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      templateId,
      dataSourceId,
      workspaceId,
      createdAt,
      updatedAt,
      versions,
    ) <> (Report.tupled, Report.unapply)
}
