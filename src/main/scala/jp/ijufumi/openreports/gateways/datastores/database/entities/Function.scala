package jp.ijufumi.openreports.gateways.datastores.database.entities

import slick.jdbc.PostgresProfile.api._
import jp.ijufumi.openreports.utils.Dates
import slick.lifted.ProvenShape

case class Function(
    id: String,
    resource: String,
    action: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Functions(tag: Tag) extends EntityBase[Function](tag, "functions") {
  def id = column[String]("id", O.PrimaryKey)
  def resource = column[String]("resource")
  def action = column[String]("action")

  override def * : ProvenShape[Function] =
    (id, resource, action, createdAt, updatedAt, versions) <> (Function.tupled, Function.unapply)
}
