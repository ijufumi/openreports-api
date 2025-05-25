package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates

case class Report(
                   id: String,
                   name: String,
                   reportTemplateId: String,
                   dataSourceId: Option[String],
                   workspaceId: String,
                   createdAt: Long = Dates.currentTimestamp(),
                   updatedAt: Long = Dates.currentTimestamp(),
                   versions: Long = 1,
)

class Reports(tag: Tag)
    extends EntityBase[Report](
      tag,
      "reports",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def reportTemplateId = column[String]("report_template_id")
  def dataSourceId = column[Option[String]]("data_source_id")
  def workspaceId = column[String]("workspace_id")

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
