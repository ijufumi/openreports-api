package jp.ijufumi.openreports.infrastructure.persistence.converter

import jp.ijufumi.openreports.domain.models.entity.{StorageS3 => StorageS3Model}
import jp.ijufumi.openreports.infrastructure.persistence.entity.{StorageS3 => StorageS3Entity}
import jp.ijufumi.openreports.utils.Crypto

object StorageS3Converter {
  def toDomain(entity: StorageS3Entity): StorageS3Model = {
    StorageS3Model(
      entity.id,
      entity.workspaceId,
      Crypto.decrypt(entity.awsAccessKeyId),
      Crypto.decrypt(entity.awsSecretAccessKey),
      entity.awsRegion,
      entity.s3BucketName,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  def toEntity(model: StorageS3Model): StorageS3Entity = {
    StorageS3Entity(
      model.id,
      model.workspaceId,
      Crypto.encrypt(model.awsAccessKeyId),
      Crypto.encrypt(model.awsSecretAccessKey),
      model.awsRegion,
      model.s3BucketName,
      model.createdAt,
      model.updatedAt,
      model.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toStorageS3Entity(model: StorageS3Model): StorageS3Entity = toEntity(model)
    implicit def fromStorageS3Entity(entity: StorageS3Entity): StorageS3Model = toDomain(entity)
    implicit def fromStorageS3Entity2(entity: Option[StorageS3Entity]): Option[StorageS3Model] =
      entity.map(toDomain)
    implicit def fromStorageS3Entities(entity: Seq[StorageS3Entity]): Seq[StorageS3Model] =
      entity.map(toDomain)
  }
}
