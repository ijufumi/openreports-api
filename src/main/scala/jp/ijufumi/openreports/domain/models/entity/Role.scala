package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.RoleTypes
import jp.ijufumi.openreports.utils.Dates

case class Role(
    id: String,
    roleType: RoleTypes.RoleType,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)
