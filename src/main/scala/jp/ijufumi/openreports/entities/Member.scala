package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import java.sql.Timestamp
import java.time.LocalDateTime

case class Member(
    id: Option[Int] = None,
    googleId: String = "",
    emailAddress: String,
    password: String = "",
    name: String,
    isAdmin: String = "1",
    createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    updatedAt: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    version: Long = 1,
)

class Members(tag: Tag)
    extends Table[Member](
      tag,
      "members",
    ) {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def googleId: Rep[String] = column[String]("google_id", O.Unique)
  def emailAddress: Rep[String] = column[String]("email_address", O.Unique)
  def password: Rep[String] = column[String]("password")
  def name: Rep[String] = column[String]("name")
  def isAdmin: Rep[String] = column[String]("is_admin")
  def createdAt: Rep[Timestamp] = column[Timestamp]("created_at")(timestampType)
  def updatedAt: Rep[Timestamp] = column[Timestamp]("updated_at")(timestampType)
  def version: Rep[Long] = column[Long]("version")

  override def * : ProvenShape[Member] =
    (
      id.?,
      googleId,
      emailAddress,
      password,
      name,
      isAdmin,
      createdAt,
      updatedAt,
      version,
    ) <> (Member.tupled, Member.unapply)
}
