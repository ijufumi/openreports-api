package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class StorageS3(
    id: String,
    workspaceId: String,
    awsAccessKeyId: String = "",
    awsSecretAccessKey: String = "",
    awsRegion: String = "",
    s3BucketName: String = "",
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class StorageS3s(tag: Tag) extends EntityBase[StorageS3](tag, "storage_s3s") {
  def id = column[String]("id", O.PrimaryKey)
  def workspaceId = column[String]("workspace_id")
  def awsAccessKeyId = column[String]("aws_access_key_id")
  def awsSecretAccessKey = column[String]("aws_secret_access_key")
  def awsRegion = column[String]("aws_region")
  def s3BucketName = column[String]("s3_bucket_name")

  def * = (
    id,
    workspaceId,
    awsAccessKeyId,
    awsSecretAccessKey,
    awsRegion,
    s3BucketName,
    createdAt,
    updatedAt,
    versions,
  ) <> (StorageS3.tupled, StorageS3.unapply)
}
