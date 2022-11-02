package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class Storage(
    id: String,
    workspaceId: String,
    awsAccessKey: String,
    awsSecretKeyId: String,
    awsRegion: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Storages(tag: Tag) extends EntityBase[Storage](tag, "storages") {
  def id = column[String]("id", O.PrimaryKey)
  def workspaceId = column[String]("workspace_id")
  def awsAccessKey = column[String]("aws_access_key")
  def awsSecretKeyId = column[String]("aws_secret_key_id")
  def awsRegion = column[String]("aws_region")

  def * = (
    id,
    workspaceId,
    awsAccessKey,
    awsSecretKeyId,
    awsRegion,
    createdAt,
    updatedAt,
    versions,
  ) <> (Storage.tupled, Storage.unapply)
}
