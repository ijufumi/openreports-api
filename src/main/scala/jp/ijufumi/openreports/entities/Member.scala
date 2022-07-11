package jp.ijufumi.openreports.entities

import slick.lifted.Tag
import slick.ast.ScalaBaseType.{intType, longType, stringType}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp

case class Member(
    id: Int,
    emailAddress: String,
    password: String,
    name: String,
    isAdmin: String,
    createdAt: Timestamp,
    updatedAt: Timestamp,
    version: Long,
)

class Members(tag: Tag)
    extends Table[Member](
      tag,
      "members",
    ) {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def emailAddress = column[String]("email_address", O.Unique)
  def password = column[String]("password")
  def name = column[String]("name")
  def isAdmin = column[String]("is_admin")
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")
  def version = column[Long]("version")

  override def * = (
    id,
    emailAddress,
    password,
    name,
    isAdmin,
    createdAt,
    updatedAt,
    version,
  ) <> (Member.tupled, Member.unapply)
}
