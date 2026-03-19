package jp.ijufumi.openreports.domain.models.entity

case class Permission(workspaces: Seq[Workspace], functions: Seq[Function])
