package jp.ijufumi.openreports.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{Member => MemberEntity}

case class Member(
    id: String,
    email: String,
    name: String,
    apiToken: String = "",
    workspaces: Seq[Workspace] = Seq.empty,
)

object Member {
  def apply(member: MemberEntity, workspaces: Seq[Workspace], apiToken: String): Member = {
    Member(member.id, member.email, member.name, apiToken, workspaces)
  }
}
