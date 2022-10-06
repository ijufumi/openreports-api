package jp.ijufumi.openreports.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime

case class Member(
    id: String,
    googleId: Option[String] = None,
    email: String,
    password: String = "",
    name: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class Members(tag: Tag)
    extends Table[Member](
      tag,
      "members",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def googleId = column[String]("google_id", O.Unique)
  def email = column[String]("email", O.Unique)
  def password = column[String]("password")
  def name = column[String]("name")
  def createdAt = column[Long]("created_at")
  def updatedAt = column[Long]("updated_at")
  def versions = column[Long]("versions")

  override def * =
    (
      id,
      googleId.?,
      email,
      password,
      name,
      createdAt,
      updatedAt,
      versions,
    ) <> (Member.tupled, Member.unapply)
}
