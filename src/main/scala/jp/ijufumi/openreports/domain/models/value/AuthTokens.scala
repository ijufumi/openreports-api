package jp.ijufumi.openreports.domain.models.value

case class AuthTokens(
    accessToken: String,
    refreshToken: Option[String],
)
