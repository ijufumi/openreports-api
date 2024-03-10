package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  Member => MemberEntity,
  WorkspaceMember => WorkspaceMemberEntity,
}
import jp.ijufumi.openreports.presentation.models.responses.{
  WorkspaceMember => WorkspaceMemberResponse,
}
import jp.ijufumi.openreports.presentation.models.requests.UpdateWorkspaceMember
import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.domain.models.entity.Member.conversions._

case class WorkspaceMember(
    workspaceId: String,
    memberId: String,
    roleId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
    member: Option[Member] = None,
) {
  def toEntity: WorkspaceMemberEntity = {
    WorkspaceMemberEntity(
      this.workspaceId,
      this.memberId,
      this.roleId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def toResponse: WorkspaceMemberResponse = {
    WorkspaceMemberResponse(
      this.workspaceId,
      this.memberId,
      this.roleId,
      this.member,
    )
  }
  def copyForUpdate(input: UpdateWorkspaceMember): WorkspaceMember = {
    this.copy(roleId = input.roleId)
  }
}

object WorkspaceMember {
  def apply(entity: (WorkspaceMemberEntity, MemberEntity)): WorkspaceMember = {
    WorkspaceMember(
      entity._1.workspaceId,
      entity._1.memberId,
      entity._1.roleId,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      Some(Member(entity._2)),
    )
  }
  def apply(entity: WorkspaceMemberEntity): WorkspaceMember = {
    WorkspaceMember(
      entity.workspaceId,
      entity.memberId,
      entity.roleId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toWorkspaceMemberEntity(model: WorkspaceMember): WorkspaceMemberEntity = {
      model.toEntity
    }

    implicit def fromWorkspaceMemberEntity(entity: WorkspaceMemberEntity): WorkspaceMember = {
      WorkspaceMember(entity)
    }

    implicit def fromWorkspaceMemberEntity2(entity: Option[WorkspaceMemberEntity]): Option[WorkspaceMember] = {
      entity.map(e => WorkspaceMember(e))
    }

    implicit def fromWorkspaceMemberEntity3(entity: (WorkspaceMemberEntity, MemberEntity)): WorkspaceMember = {
      WorkspaceMember(entity)
    }

    implicit def fromWorkspaceMemberEntity4(entity: Option[(WorkspaceMemberEntity, MemberEntity)]): Option[WorkspaceMember] = {
      entity.map(e => WorkspaceMember(e))
    }

    implicit def fromWorkspaceMemberEntities(entity: Seq[WorkspaceMemberEntity]): Seq[WorkspaceMember] = {
      entity.map(e => WorkspaceMember(e))
    }

    implicit def fromWorkspaceMemberEntities2(entity: Seq[(WorkspaceMemberEntity, MemberEntity)]): Seq[WorkspaceMember] = {
      entity.map(e => WorkspaceMember(e))
    }

    implicit def toWorkspaceMemberResponse(model: WorkspaceMember): WorkspaceMemberResponse = {
      model.toResponse
    }

    implicit def toWorkspaceMemberResponse2(model: Option[WorkspaceMember]): Option[WorkspaceMemberResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toWorkspaceMemberResponses(model: Seq[WorkspaceMember]): Seq[WorkspaceMemberResponse] = {
      model.map(m => m.toResponse)
    }
  }
}
