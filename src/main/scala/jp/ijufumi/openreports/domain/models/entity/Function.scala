package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.ActionTypes
import jp.ijufumi.openreports.utils.Dates

case class Function(
    id: String,
    resource: String,
    action: ActionTypes.ActionType,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
)
