package jp.ijufumi.openreports.infrastructure.datastores.database

import jp.ijufumi.openreports.domain.models.value.enums.{
  ActionTypes,
  EmbeddedFunctionTypes,
  JdbcDriverClasses,
  ParameterTypes,
  RoleTypes,
  StorageTypes,
}
import slick.jdbc.JdbcType
import slick.ast.BaseTypedType

package object entities {
  import slick.jdbc.PostgresProfile.api._

  abstract class EntityBase[T](tag: Tag, tableName: String) extends Table[T](tag, tableName) {
    def createdAt = column[Long]("created_at")
    def updatedAt = column[Long]("updated_at")
    def versions = column[Long]("versions")
  }

  implicit val jdbcDriverClassMapper: JdbcType[JdbcDriverClasses.JdbcDriverClass]
    with BaseTypedType[JdbcDriverClasses.JdbcDriverClass] =
    MappedColumnType.base[JdbcDriverClasses.JdbcDriverClass, String](
      e => e.toString,
      s => JdbcDriverClasses.withName(s),
    )

  implicit val roleTypeMapper: JdbcType[RoleTypes.RoleType] with BaseTypedType[RoleTypes.RoleType] =
    MappedColumnType.base[RoleTypes.RoleType, String](
      e => e.toString,
      s => RoleTypes.withName(s),
    )

  implicit val storageTypeMapper
      : JdbcType[StorageTypes.StorageType] with BaseTypedType[StorageTypes.StorageType] =
    MappedColumnType.base[StorageTypes.StorageType, String](
      e => e.toString,
      s => StorageTypes.withName(s),
    )

  implicit val actionTypeMapper
      : JdbcType[ActionTypes.ActionType] with BaseTypedType[ActionTypes.ActionType] = {
    MappedColumnType.base[ActionTypes.ActionType, String](
      e => e.toString,
      s => ActionTypes.withName(s),
    )
  }

  implicit val parameterTypeMapper
      : JdbcType[ParameterTypes.ParameterType] with BaseTypedType[ParameterTypes.ParameterType] = {
    MappedColumnType.base[ParameterTypes.ParameterType, String](
      e => e.toString,
      s => ParameterTypes.withName(s),
    )
  }

  implicit val embeddedFunctionTypeMapper: JdbcType[EmbeddedFunctionTypes.EmbeddedFunctionType]
    with BaseTypedType[EmbeddedFunctionTypes.EmbeddedFunctionType] = {
    MappedColumnType.base[EmbeddedFunctionTypes.EmbeddedFunctionType, String](
      e => e.toString,
      s => EmbeddedFunctionTypes.withName(s),
    )
  }
}
