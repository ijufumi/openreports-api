package jp.ijufumi.openreports.infrastructure.datastores.database.entities

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class RefreshToken(
    id: String,
    memberId: String,
    refreshToken: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)

class RefreshTokens(tag: Tag)
    extends EntityBase[RefreshToken](
      tag,
      "refresh_tokens",
    ) {
  def id = column[String]("id", O.PrimaryKey)
  def memberId = column[String]("member_id")
  def refreshToken = column[String]("refresh_token")

  override def * =
    (
      id,
      memberId,
      refreshToken,
      createdAt,
      updatedAt,
      versions,
    ) <> (RefreshToken.tupled, RefreshToken.unapply)
}
