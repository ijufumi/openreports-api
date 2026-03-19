package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.Dates

case class WorkspaceMember(
    workspaceId: String,
    memberId: String,
    roleId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    member: Option[Member] = None,
)
