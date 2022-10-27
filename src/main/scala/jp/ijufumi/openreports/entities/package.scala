package jp.ijufumi.openreports

package object entities {
  import slick.jdbc.PostgresProfile.api._

  abstract class EntityBase[T](tag: Tag, tableName: String) extends Table[T](tag, tableName) {
    def createdAt = column[Long]("created_at")
    def updatedAt = column[Long]("updated_at")
    def versions = column[Long]("versions")
  }
}
