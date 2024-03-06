package jp.ijufumi.openreports.domain.models.entity

import jp.ijufumi.openreports.domain.models.value.enums.{EmbeddedFunctionTypes, ParameterTypes}
import jp.ijufumi.openreports.infrastructure.datastores.database.entities.{
  ReportParameter => ReportParameterEntity,
}
import jp.ijufumi.openreports.presentation.models.responses.{
  ReportParameter => ReportParameterResponse,
}
import jp.ijufumi.openreports.utils.Dates

case class ReportParameter(
    id: String,
    workspaceId: String,
    parameterType: ParameterTypes.ParameterType,
    embeddedFunctionType: Option[EmbeddedFunctionTypes.EmbeddedFunctionType],
    value: Option[String],
    createdAt: Long = Dates.currentTimestamp(),
    updatedAt: Long = Dates.currentTimestamp(),
    versions: Long = 1,
) {
  def toEntity: ReportParameterEntity = {
    ReportParameterEntity(
      this.id,
      this.workspaceId,
      this.parameterType,
      this.embeddedFunctionType,
      this.value,
      this.createdAt,
      this.updatedAt,
      this.versions,
    )
  }
  def toResponse: ReportParameterResponse = {
    ReportParameterResponse(
      this.id,
      this.parameterType,
      this.embeddedFunctionType,
      this.value,
    )
  }
}

object ReportParameter {
  def apply(entity: ReportParameterEntity): ReportParameter = {
    ReportParameter(
      entity.id,
      entity.workspaceId,
      entity.parameterType,
      entity.embeddedFunctionType,
      entity.value,
      entity.createdAt,
      entity.updatedAt,
    )
  }

  object conversions {
    import scala.language.implicitConversions

    implicit def toReportParameterEntity(model: ReportParameter): ReportParameterEntity = {
      model.toEntity
    }

    implicit def fromReportParameterEntity(entity: ReportParameterEntity): ReportParameter = {
      ReportParameter(entity)
    }

    implicit def fromReportParameterEntity2(
        entity: Option[ReportParameterEntity],
    ): Option[ReportParameter] = {
      entity.map(e => ReportParameter(e))
    }

    implicit def fromReportParameterEntities(
        entity: Seq[ReportParameterEntity],
    ): Seq[ReportParameter] = {
      entity.map(e => ReportParameter(e))
    }

    implicit def toReportParameterResponse(model: ReportParameter): ReportParameterResponse = {
      model.toResponse
    }

    implicit def toReportParameterResponse2(
        model: Option[ReportParameter],
    ): Option[ReportParameterResponse] = {
      model.map(m => m.toResponse)
    }

    implicit def toReportParameterResponses(
        model: Seq[ReportParameter],
    ): Seq[ReportParameterResponse] = {
      model.map(m => m.toResponse)
    }
  }
}
