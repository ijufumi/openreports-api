package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.Dates

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
