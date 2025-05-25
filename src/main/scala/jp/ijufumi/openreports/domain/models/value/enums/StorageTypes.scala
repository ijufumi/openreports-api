package jp.ijufumi.openreports.domain.models.value.enums

object StorageTypes extends Enumeration {
  type StorageType = Value

  val Local: StorageTypes.Value = Value("local")
  val LocalSeed: StorageTypes.Value = Value("localSeed")
  val S3: StorageTypes.Value = Value("s3")
}
