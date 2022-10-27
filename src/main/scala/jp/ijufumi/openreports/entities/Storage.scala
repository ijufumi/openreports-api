package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class Storage(
    id: String,
    workspaceId: String,
    awsAccessKey: String,
    awsSecretKeyId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Storages(tag: Tag) extends EntityBase[Storage](tag, "storages") {
  def id = column[String]("id", O.PrimaryKey)
  def workspaceId = column[String]("workspace_id")
  def awsAccessKey = column[String]("workspace_id")
  def awsSecretKeyId = column[String]("workspace_id")

  def * = (
    id,
    workspaceId,
    awsAccessKey,
    awsSecretKeyId,
    createdAt,
    updatedAt,
    versions,
  ) <> (Storage.tupled, Storage.unapply)
}
