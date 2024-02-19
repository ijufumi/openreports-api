package jp.ijufumi.openreports.domain.models.value.enums

object StorageTypes extends Enumeration {
  type StorageType = Value

  val Local: StorageTypes.Value = Value("local")
  val S3: StorageTypes.Value = Value("s3")
}
