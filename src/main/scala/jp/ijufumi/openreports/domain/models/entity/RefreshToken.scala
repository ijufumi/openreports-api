package jp.ijufumi.openreports.domain.models.entity

case class RefreshToken(
    id: String,
    memberId: String,
    refreshToken: String,
    isUsed: Boolean,
    createdAt: Long,
    updatedAt: Long,
    version: Long,
)
