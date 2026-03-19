package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.utils.Dates

case class Member(
    id: String,
    googleId: Option[String],
    email: String,
    password: String,
    name: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    workspaces: Seq[Workspace] = Seq.empty,
)
