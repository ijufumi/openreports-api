package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  ReportGroup => ReportGroupEntity,
}
import jp.ijufumi.openreports.presentation.models.requests.UpdateReportGroup
import jp.ijufumi.openreports.utils.Dates
import jp.ijufumi.openreports.presentation.models.responses.{ReportGroup => ReportGroupResponse}

case class ReportGroup(
    id: String,
    name: String,
    workspaceId: String,
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: ReportGroupEntity = {
    ReportGroupEntity(
      this.id,
      this.name,
      this.workspaceId,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def toResponse: ReportGroupResponse = {
    ReportGroupResponse(
      this.id,
      this.name,
    )
  }
  def copyForUpdate(input: UpdateReportGroup): ReportGroup = {
    this.copy(name = input.name)
  }
}

object ReportGroup {
  def apply(entity: ReportGroupEntity): ReportGroup = {
    ReportGroup(
      entity.id,
      entity.name,
      entity.workspaceId,
      entity.createdAt,
      entity.updatedAt,
      entity.versions,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toReportGroupEntity(model: ReportGroup): ReportGroupEntity = {
      model.toEntity
    }

    implicit def toReportGroupEntities(model: Seq[ReportGroup]): Seq[ReportGroupEntity] = {
      model.map(m => m.toEntity)
    }

    implicit def fromReportGroupEntity(entity: ReportGroupEntity): ReportGroup = {
      ReportGroup(entity)
    }

    implicit def fromReportGroupEntity2(entity: Option[ReportGroupEntity]): Option[ReportGroup] = {
      entity.map(e => ReportGroup(e))
    }

    implicit def fromReportGroupEntities(entity: Seq[ReportGroupEntity]): Seq[ReportGroup] = {
      entity.map(e => ReportGroup(e))
    }

    implicit def toReportGroupResponse(model: ReportGroup): ReportGroupResponse = {
      model.toResponse
    }

    implicit def toReportGroupResponse2(model: Option[ReportGroup]): Option[ReportGroupResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toReportGroupResponses(model: Seq[ReportGroup]): Seq[ReportGroupResponse] = {
      model.map(m => m.toResponse)
    }

  }
}
