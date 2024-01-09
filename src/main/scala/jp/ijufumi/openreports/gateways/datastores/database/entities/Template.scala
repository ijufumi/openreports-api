package jp.ijufumi.openreports.gateways.datastores.database.entities

import jp.ijufumi.openreports.gateways.datastores.database.entities.enums.StorageTypes._
import jp.ijufumi.openreports.models.inputs.UpdateTemplate
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class Template(
    id: String,
    name: String,
    filePath: String,
    workspaceId: String,
    storageType: StorageType,
    fileSize: Long,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def copyForUpdate(input: UpdateTemplate): Template = {
    this.copy(name = input.name)
  }
}

class Templates(tag: Tag)
    extends EntityBase[Template](
      tag,
      "templates",
    ) {

  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def filePath = column[String]("file_path")
  def workspaceId = column[String]("workspace_id")
  def storageType = column[StorageType]("storage_type")
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
    ) <> (Template.tupled, Template.unapply)
}
