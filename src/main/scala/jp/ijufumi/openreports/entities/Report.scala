package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime

case class Report(
    id: String,
    name: String,
    reportTemplateId: String,
    createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
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
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      name,
      reportTemplateId,
      createdAt,
      updatedAt,
      versions,
    ) <> (Report.tupled, Report.unapply)
}
