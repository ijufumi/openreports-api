package jp.ijufumi.openreports.infrastructure.persistence.entity

import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.PostgresProfile.api._

case class RefreshToken(
    id: String,
    memberId: String,
    refreshToken: String,
    expiredAt: Long,
    usedAt: Option[Long] = None,
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
  def refreshToken = column[String]("refresh_token", O.Unique)
  def expiredAt = column[Long]("expired_at")
  def usedAt = column[Option[Long]]("used_at")

  override def * =
    (
      id,
      memberId,
      refreshToken,
      expiredAt,
      usedAt,
      createdAt,
      updatedAt,
      versions,
    ).mapTo[RefreshToken]
}
