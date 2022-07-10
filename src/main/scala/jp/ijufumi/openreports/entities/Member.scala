package jp.ijufumi.openreports.entities

import slick.jdbc.PostgresProfile._
import slick.lifted.Tag
import slick.ast.ScalaBaseType.{intType, longType, stringType}

import java.sql.Timestamp

class Member(tag: Tag)
    extends Table[(Int, String, String, String, String, Timestamp, Timestamp, Long)](
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

  def * = (
    id,
    emailAddress,
    password,
    name,
    isAdmin,
    createdAt,
    updatedAt,
    version,
  )
}
