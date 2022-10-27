package jp.ijufumi.openreports.entities.enums

import slick.jdbc.PostgresProfile.api._

object StorageTypes extends Enumeration {
  type StorageType = Value

  val Local = Value("local")
  val S3 = Value("s3")

  implicit val storageTypeMapper = MappedColumnType.base[StorageTypes.StorageType, String](
    e => e.toString,
    s => StorageTypes.withName(s)
  )
}
