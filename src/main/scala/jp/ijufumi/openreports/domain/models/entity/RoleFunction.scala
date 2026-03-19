package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.Dates

case class RoleFunction(
    id: String,
    roleId: String,
    functionId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)
