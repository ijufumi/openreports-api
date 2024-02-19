package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.domain.models.value.enums.ActionTypes
import slick.lifted.ProvenShape

case class Function(
    id: String,
    resource: String,
    action: ActionTypes.ActionType,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Functions(tag: Tag) extends EntityBase[Function](tag, "functions") {
  def id = column[String]("id", O.PrimaryKey)
  def resource = column[String]("resource")
  def action = column[ActionTypes.ActionType]("action")

  override def * : ProvenShape[Function] =
    (id, resource, action, createdAt, updatedAt, versions) <> (Function.tupled, Function.unapply)
}
