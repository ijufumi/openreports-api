package jp.ijufumi.openreports.interfaces.models.outputs

import jp.ijufumi.openreports.gateways.datastores.database.entities.{Member => MemberEntity}
import jp.ijufumi.openreports.utils.Dates
import org.json4s.FieldSerializer

case class Member(
    id: String,
    googleId: Option[String],
    email: String,
    password: String,
    name: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    apiToken: String = "",
    workspaces: Seq[Workspace] = Seq.empty,
) {
  def toEntity: MemberEntity = {
    MemberEntity(
      this.id,
      this.googleId,
      this.email,
      this.password,
      this.name,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def withApiToken(apiToken: String): Member = {
    copy(apiToken = apiToken)
  }
  def withWorkspace(workspaces: Seq[Workspace]): Member = {
    copy(workspaces = workspaces)
  }
}

object Member {
  val memberSerializer: FieldSerializer[Member] = FieldSerializer[Member](
    FieldSerializer.ignore("password") orElse FieldSerializer.ignore("googleId"),
  )

  def apply(member: MemberEntity, workspaces: Seq[Workspace], apiToken: String): Member = {
    Member(
      member.id,
      member.googleId,
      member.email,
      member.password,
      member.name,
      member.createdAt,
      member.updatedAt,
      member.versions,
      apiToken,
      workspaces,
    )
  }
  def apply(member: MemberEntity): Member = {
    Member(
      member.id,
      member.googleId,
      member.email,
      member.password,
      member.name,
      member.createdAt,
      member.updatedAt,
      member.versions,
    )
  }
}
