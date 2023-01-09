package jp.ijufumi.openreports.entities.enums

import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.jdbc.PostgresProfile.api._

object StorageTypes extends Enumeration {
  type StorageType = Value

  val Local: StorageTypes.Value = Value("local")
  val S3: StorageTypes.Value = Value("s3")

  implicit val storageTypeMapper: JdbcType[StorageType] with BaseTypedType[StorageType] =
    MappedColumnType.base[StorageTypes.StorageType, String](
      e => e.toString,
      s => StorageTypes.withName(s),
    )
}
