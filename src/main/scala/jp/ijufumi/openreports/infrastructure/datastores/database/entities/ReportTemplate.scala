package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import jp.ijufumi.openreports.domain.models.value.enums.StorageTypes
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class ReportTemplate(
    id: String,
    name: String,
    filePath: String,
    workspaceId: String,
    storageType: StorageTypes.StorageType,
    fileSize: Long,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class ReportTemplates(tag: Tag)
    extends EntityBase[ReportTemplate](
      tag,
      "templates",
    ) {

  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def filePath = column[String]("file_path")
  def workspaceId = column[String]("workspace_id")
  def storageType = column[StorageTypes.StorageType]("storage_type")
  def fileSize = column[Long]("file_size")

  override def * =
    (
      id,
      name,
      filePath,
      workspaceId,
      storageType,
      fileSize,
      createdAt,
      updatedAt,
      versions,
    ) <> (ReportTemplate.tupled, ReportTemplate.unapply)
}
