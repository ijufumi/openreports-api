package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{Member => MemberEntity}
import jp.ijufumi.openreports.presentation.models.responses.{Member => MemberResponse}
import jp.ijufumi.openreports.utils.Dates
import scala.language.implicitConversions

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
  def toResponse: MemberResponse = {
    MemberResponse(
      this.id,
      this.email,
      this.name,
    )
  }
}

object Member {
  def apply(entity: (MemberEntity, Seq[Workspace]), apiToken: String): Member = {
    Member(
      entity._1.id,
      entity._1.googleId,
      entity._1.email,
      entity._1.password,
      entity._1.name,
      entity._1.createdAt,
      entity._1.updatedAt,
      entity._1.versions,
      apiToken,
      entity._2,
    )
  }
  def apply(entity: MemberEntity): Member = {
    Member(
      entity.id,
      entity.googleId,
      entity.email,
      entity.password,
      entity.name,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  object conversions {

    implicit def fromMemberEntity(entity: MemberEntity): Member = {
      Member(entity)
    }

    implicit def fromMemberEntity2(entity: Option[MemberEntity]): Option[Member] = {
      entity.map(e => Member(e))
    }

    implicit def fromMemberEntities(entity: Seq[MemberEntity]): Seq[Member] = {
      entity.map(e => Member(e))
    }

    implicit def toMemberEntity(model: Member): MemberEntity = {
      model.toEntity
    }

    implicit def toMemberResponse(model: Member): MemberResponse = {
      model.toResponse
    }

    implicit def toMemberResponse2(model: Option[Member]): Option[MemberResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toMemberResponses(model: Seq[Member]): Seq[MemberResponse] = {
      model.map(m => m.toResponse)
    }
  }
}
