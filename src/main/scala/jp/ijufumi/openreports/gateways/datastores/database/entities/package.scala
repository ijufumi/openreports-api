package jp.ijufumi.openreports.gateways.datastores.database

import slick.jdbc.JdbcType
import jp.ijufumi.openreports.models.value.enums.ActionTypes
import slick.ast.BaseTypedType

package object entities {
  import slick.jdbc.PostgresProfile.api._

  abstract class EntityBase[T](tag: Tag, tableName: String) extends Table[T](tag, tableName) {
    def createdAt = column[Long]("created_at")
    def updatedAt = column[Long]("updated_at")
    def versions = column[Long]("versions")
  }

  implicit val actionTypeMapper
      : JdbcType[ActionTypes.ActionType] with BaseTypedType[ActionTypes.ActionType] = {
    MappedColumnType.base[ActionTypes.ActionType, String](
      e => e.toString,
      s => ActionTypes.withName(s),
    )
  }
}
