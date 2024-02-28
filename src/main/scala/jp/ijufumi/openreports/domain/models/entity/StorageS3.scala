package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  StorageS3 => StorageS3Entity,
}
import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.presentation.models.responses.{StorageS3 => StorageS3Response}

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
) {
  def toEntity: StorageS3Entity = {
    StorageS3Entity(
      this.id,
      this.workspaceId,
      this.awsAccessKeyId,
      this.awsSecretAccessKey,
      this.awsRegion,
      this.s3BucketName,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def toResponse: StorageS3Response = {
    StorageS3Response(
      this.id,
      this.s3BucketName,
    )
  }
}

object StorageS3 {
  def apply(entity: StorageS3Entity): StorageS3 = {
    StorageS3(
      entity.id,
      entity.workspaceId,
      entity.awsAccessKeyId,
      entity.awsSecretAccessKey,
      entity.awsRegion,
      entity.s3BucketName,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }
}
